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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Adapters.ClienteProductosRecyclerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.util.LinkedList;
import java.util.List;

public class ClienteProductosFragment extends Fragment {

    public static final String TAG = "ClienteProductosFragment";

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String clienteID; //ID del fabricante

    RecyclerView rvProductos;
    ClienteProductosRecyclerAdapter clienteProductosRecyclerAdapter;
    List<Producto> productos = new LinkedList<>();

    View view;

    public ClienteProductosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cliente_fragment_productos, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            clienteID = user.getUid();

        rvProductos = (RecyclerView) view.findViewById(R.id.rvProductos);

        cargarProductos();

        clienteProductosRecyclerAdapter = new ClienteProductosRecyclerAdapter(getContext(), productos, mListener);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductos.setAdapter(clienteProductosRecyclerAdapter);

        return view;
    }

    private void cargarProductos(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productosDatabase  = database.getReference("productos");

        productosDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.removeAll(productos);
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    Producto producto = snapshot.getValue(Producto.class);
                    productos.add(producto);
                }
                clienteProductosRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
