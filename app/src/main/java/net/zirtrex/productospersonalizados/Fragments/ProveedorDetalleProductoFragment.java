package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.text.DecimalFormat;

public class ProveedorDetalleProductoFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "ProveedorDetProFragment";

    private OnProveedorFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String proveedorID; //ID del fabricante
    private String idProducto;

    ImageView tvImagenProducto;
    TextView tvNombreProducto, tvPrecio;
    Button btnEditarProducto;

    View view;

    public ProveedorDetalleProductoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_detalle_producto, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            proveedorID = user.getUid();

        if (getArguments() != null) {
            idProducto = getArguments().getString("idProducto");
            getProductoSeleccionado(idProducto);
        }

        return view;
    }

    private void getProductoSeleccionado(String idProducto) {

        productosDatabase  = FirebaseDatabase.getInstance().getReference("productos").child(idProducto);

        productosDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto inProducto = dataSnapshot.getValue(Producto.class);
                actualizarVista(inProducto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void actualizarVista(Producto producto) {
        if (producto != null && view != null) {

            getActivity().setTitle(producto.getNombreProducto());

            tvNombreProducto = (TextView) view.findViewById(R.id.tvNombreProducto);
            tvPrecio = (TextView) view.findViewById(R.id.tvPrecio);
            tvImagenProducto = (ImageView) view.findViewById(R.id.ivCartImagenProducto);
            btnEditarProducto = (Button) view.findViewById(R.id.btnEditarProducto);

            tvNombreProducto.setText(producto.getNombreProducto());
            DecimalFormat precision = new DecimalFormat("0.00");
            //tvPrecio.setText("$" + precision.format(producto.getPrecio()));

            Glide.with(getContext())
                    .load(producto.getImgUrl())
                    .centerCrop()
                    .placeholder(R.drawable.load)
                    .into(tvImagenProducto);

            btnEditarProducto.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnEditarProducto:
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
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
