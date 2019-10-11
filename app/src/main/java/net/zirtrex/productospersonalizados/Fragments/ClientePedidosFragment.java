package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Adapters.PedidosAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Cart;
import net.zirtrex.productospersonalizados.Models.Pedidos;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientePedidosFragment extends Fragment{

    public static final String TAG = "ClientePedidosFragment";

    OnFragmentInteractionListener mListener;

    private DatabaseReference pedidosDatabase;
    private String clienteID; //ID del cliente

    List<Pedidos> lPedidos = new ArrayList<>();

    PedidosAdapter pedidosAdapter;
    RecyclerView rvPedidos;

    Double precioTotal = 0.00;

    TextView tvPrecioTotal;

    View view;

    public ClientePedidosFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cliente_fragment_pedidos, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            clienteID = user.getUid();

            obtenerPedidos();

            rvPedidos = (RecyclerView) view.findViewById(R.id.rvPedidos);
            rvPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));

            pedidosAdapter = new PedidosAdapter(getContext(), lPedidos);
            rvPedidos.setAdapter(pedidosAdapter);

            itemTouchHelper.attachToRecyclerView(rvPedidos);
        }

        return view;

    }

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

            int position = viewHolder.getAdapterPosition();
            Pedidos pedido = lPedidos.get(position);
            String idPedido = pedido.getIdPedido();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                DatabaseReference pedidosDatabase = FirebaseDatabase.getInstance().getReference("pedidos").child(idPedido);
                pedidosDatabase.removeValue();
                lPedidos.remove(position);
                pedidosAdapter.notifyDataSetChanged();
                calculateTotal(lPedidos);
                Toast.makeText(viewHolder.itemView.getContext(), "Pedido eliminado correctamente",
                        Toast.LENGTH_LONG).show();

            }
        }
    });

    private void obtenerPedidos() {
        final int[] count = new int[1];

        if(clienteID != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosDatabase = database.getReference("pedidos");
            Query pedidosSearchQuery;

            pedidosSearchQuery = pedidosDatabase.orderByChild("idCliente").equalTo(clienteID);

            pedidosSearchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lPedidos.removeAll(lPedidos);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                        Pedidos pedido = snapshot.getValue(Pedidos.class);
                        lPedidos.add(pedido);
                    }
                    pedidosAdapter.notifyDataSetChanged();
                    calculateTotal(lPedidos);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //count = 0;
                }
            });
        }
    }

    public void calculateTotal(List<Pedidos> lPedidos){

        Double precioTotal = 0.00;

        for (int i = 0; i < lPedidos.size(); i++)
        {
            Double price = lPedidos.get(i).getTotal();

            precioTotal += price;

        }

        this.precioTotal = precioTotal;

        DecimalFormat precision = new DecimalFormat("0.00");

        TextView tvPrecioTotal = (TextView) view.findViewById(R.id.tvPrecioTotal);
        String convertPrice = precision.format(this.precioTotal);
        tvPrecioTotal.setText(convertPrice);

        if (null != mListener) {
            mListener.saveMonto(this.precioTotal);
            mListener.updateNotificationsBadge(lPedidos.size());
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        //getCart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "Se debe implementar OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
