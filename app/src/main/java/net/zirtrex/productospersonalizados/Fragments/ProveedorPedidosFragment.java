package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import net.zirtrex.productospersonalizados.Adapters.ProveedorProductosRecyclerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Pedidos;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ProveedorPedidosFragment extends Fragment {

    public static final String TAG = "ProveedorPedidosFragment";

    private OnProveedorFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String proveedorID; //ID del fabricante

    List<Pedidos> lPedidos = new ArrayList<>();

    PedidosAdapter pedidosAdapter;
    RecyclerView rvPedidos;

    Double precioTotal = 0.00;

    View view;

    public ProveedorPedidosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_pedidos, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            proveedorID = user.getUid();

            obtenerPedidos();

            rvPedidos = (RecyclerView) view.findViewById(R.id.rvPedidos);
            rvPedidos.setLayoutManager(new LinearLayoutManager(getActivity()));

            pedidosAdapter = new PedidosAdapter(getContext(), lPedidos);
            rvPedidos.setAdapter(pedidosAdapter);
        }

        return view;
    }

    private void obtenerPedidos() {
        final int[] count = new int[1];

        if(proveedorID != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosDatabase = database.getReference("pedidos");
            Query pedidosSearchQuery;

            pedidosSearchQuery = pedidosDatabase.orderByChild("idProveedor").equalTo(proveedorID);

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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
            //this.montoTotal = mListener.getMonto();
        } else {
            throw new RuntimeException(context.toString()
                    + "Se debe implementar OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
