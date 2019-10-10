package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import net.zirtrex.productospersonalizados.Models.Productos;
import net.zirtrex.productospersonalizados.Util.*;

import java.util.LinkedList;
import java.util.List;

public class ProveedorProductosFragment extends Fragment {

    public static final String TAG = "ProveedorProductosFragment";

    private OnProveedorFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String proveedorID; //ID del fabricante

    RecyclerView rvProductos;
    ProveedorProductosRecyclerAdapter recyclerAdapter;
    List<Productos> productos = new LinkedList<Productos>();

    View view;

    public ProveedorProductosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_productos, container, false);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null)
            proveedorID = mAuth.getCurrentUser().getUid();

        rvProductos = (RecyclerView) view.findViewById(R.id.rvProductos);

        cargarProductos();

        recyclerAdapter = new ProveedorProductosRecyclerAdapter(getContext(), productos, mListener);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductos.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvProductos);

        rvProductos.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        // Inflate the Fragments for this fragment
        return view;
    }

    private void cargarProductos(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        productosDatabase  = database.getReference("productos");
        Query productosSearchQuery;

        if(proveedorID != null){

            productosSearchQuery = productosDatabase.orderByChild("idUsuario").equalTo(proveedorID);

            productosSearchQuery.addValueEventListener(new ValueEventListener() {
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
        }
    }

    SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
        @Override
        public void onRightClicked(int position) {
                /*recyclerAdapter.lProductos.remove(position);
                recyclerAdapter.notifyItemRemoved(position);
                recyclerAdapter.notifyItemRangeChanged(position, recyclerAdapter.getItemCount());*/

            String idProducto = (String) recyclerAdapter.getItem(position).getIdProducto();

            if(proveedorID != null){

                DatabaseReference productosDatabase = FirebaseDatabase.getInstance().getReference("productos").child(idProducto);
                productosDatabase.removeValue();
                recyclerAdapter.notifyDataSetChanged();

                Log.w("session" , "Usuario Logueado");
                Toast.makeText(getContext(), "Producto eliminado correctamente",
                        Toast.LENGTH_LONG).show();

            }else {

                Log.i("session" , "Sin usuario activo");
                Toast.makeText(getContext(), "No tienes permiso para eliminar",
                        Toast.LENGTH_LONG).show();

            }
        }
    });

    private void setupRecyclerView() {
        // ...
        rvProductos.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debe implementarse OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
