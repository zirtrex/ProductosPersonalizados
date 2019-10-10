package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Adapters.ProveedorProductosRecyclerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.util.LinkedList;
import java.util.List;


public class ProveedorPedidosFragment extends Fragment {

    public static final String TAG = "ProveedorPedidosFragment";

    private OnProveedorFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String proveedorID; //ID del fabricante

    RecyclerView rvProductos;
    ProveedorProductosRecyclerAdapter recyclerAdapter;
    List<Producto> productos = new LinkedList<Producto>();

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

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null)
            proveedorID = mAuth.getCurrentUser().getUid();

        rvProductos = (RecyclerView) view.findViewById(R.id.rvProductos);
        recyclerAdapter = new ProveedorProductosRecyclerAdapter(getContext(), productos, mListener);

        cargarPedidos();

        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductos.setAdapter(new ProveedorProductosRecyclerAdapter(getContext(), productos, mListener));

        return view;
    }

    private void cargarPedidos(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productosDatabase  = database.getReference("pedidos");
        Query productosSearchQuery;

        if(proveedorID != null){

            productosSearchQuery = productosDatabase.orderByChild("idUsuario").equalTo(proveedorID);

            productosSearchQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    productos.removeAll(productos);
                    for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                        Producto producto = snapshot.getValue(Producto.class);
                        productos.add(producto);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
