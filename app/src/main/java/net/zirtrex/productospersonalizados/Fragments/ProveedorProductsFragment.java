package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Adapters.ProveedorRecyclerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Productos;

import java.util.LinkedList;
import java.util.List;

public class ProveedorProductsFragment extends Fragment {

    public static final String TAG = "ProveedorProductsFragment";

    private Toolbar toolbar;
    RecyclerView rvProductos;
    ProveedorRecyclerAdapter recyclerAdapter;
    List<Productos> productos;

    private OnProveedorFragmentInteractionListener mListener;

    public ProveedorProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        getActivity().setTitle(getText(R.string.title_fragment_productos));

        rvProductos = (RecyclerView) view.findViewById(R.id.rvProductos);

        productos = new LinkedList<Productos>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        database.getReference("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productos.removeAll(productos);
                for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    Productos producto = snapshot.getValue(Productos.class);
                    productos.add(producto);
                }
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerAdapter = new ProveedorRecyclerAdapter(getContext(), productos, mListener);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductos.setAdapter(recyclerAdapter);
        // Inflate the Fragments for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
