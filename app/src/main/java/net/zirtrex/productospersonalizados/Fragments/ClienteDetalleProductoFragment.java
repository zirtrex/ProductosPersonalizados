package net.zirtrex.productospersonalizados.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.Pedidos;
import net.zirtrex.productospersonalizados.Models.PrecioVentaContent;
import net.zirtrex.productospersonalizados.Models.Producto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ClienteDetalleProductoFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "ClienteDetProFragment";

    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;
    private DatabaseReference productosDatabase;
    private String clienteID; //ID del cliente
    private String idProducto;

    Producto producto;
    String nombreProducto, imgUrl;
    Double precio = 0.0;

    private int mQuantity = 1;
    private Double mTotalPrice;

    private ImageView ivCartImagenProducto;
    private TextView tvNombreProducto, tvPrecio, tvCostoTotal;
    private EditText txtAncho, txtLargo;
    private Button btnCalcularPrecio, btnAgregarProducto, btnIncrement, btnDecrement;

    View view;

    public ClienteDetalleProductoFragment() {}

    @Override
    public void onStart() {
        super.onStart();
        obtenerPedidos();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.cliente_fragment_detalle_producto, container, false);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth != null)
            clienteID = mAuth.getCurrentUser().getUid();

        if (getArguments() != null) {
            idProducto = getArguments().getString("idProducto");
            getProductoSeleccionado(idProducto);
        }

        if (mQuantity == 1){
            //mTotalPrice = precio;
            //displayCost(mTotalPrice);
        }

        return view;
    }

    private void getProductoSeleccionado(final String idProducto) {
        productosDatabase  = FirebaseDatabase.getInstance().getReference("productos").child(idProducto);
        productosDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Producto inProducto = dataSnapshot.getValue(Producto.class);
                producto = inProducto;
                actualizarVista(inProducto);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void actualizarVista(Producto producto) {
        if (producto != null && view != null) {

            //Text View
            tvNombreProducto = (TextView) view.findViewById(R.id.tvNombreProducto);
            tvPrecio = (TextView) view.findViewById(R.id.tvPrecio);
            tvCostoTotal = (TextView) view.findViewById(R.id.tvCostoTotal);
            ivCartImagenProducto = (ImageView) view.findViewById(R.id.ivCartImagenProducto);
            //Edit Text
            txtAncho = (EditText) view.findViewById(R.id.txtAncho);
            txtLargo = (EditText) view.findViewById(R.id.txtLargo);

            //Button
            btnCalcularPrecio = (Button) view.findViewById(R.id.btnCalcularPrecio);
            btnAgregarProducto = (Button) view.findViewById(R.id.btnAgregarProducto);
            btnIncrement = (Button) view.findViewById(R.id.btnIncrement);
            btnDecrement = (Button) view.findViewById(R.id.btnDecrement);

            tvNombreProducto.setText(producto.getNombreProducto());
            DecimalFormat precision = new DecimalFormat("0.00");
            //tvPrecio.setText("$" + precision.format(producto.getPrecio()));

            Glide.with(getContext())
                    .load(producto.getImgUrl())
                    .centerCrop()
                    .placeholder(R.drawable.load)
                    .into(ivCartImagenProducto);

            btnCalcularPrecio.setOnClickListener(this);
            btnAgregarProducto.setOnClickListener(this);
            btnIncrement.setOnClickListener(this);
            btnDecrement.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCalcularPrecio:
                verificarAnchoLargo();
                break;
            case R.id.btnAgregarProducto:
                agregarPedido();
                break;
            case R.id.btnIncrement:
                increment();
                break;
            case R.id.btnDecrement:
                decrement();
                break;
            default:
        }
    }

    public void agregarPedido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Desea comprar la prenda?");
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                guardarPedido();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void guardarPedido() {
        if(clienteID != null){

            DatabaseReference pedidosDatabase = FirebaseDatabase.getInstance().getReference("pedidos");
            Pedidos pedido = new Pedidos();
            String idPedido = pedidosDatabase.push().getKey();

            pedido.setIdPedido(idPedido);
            pedido.setIdCliente(clienteID);
            pedido.setIdProveedor(producto.getIdUsuario());
            pedido.setIdProducto(producto.getIdProducto());
            pedido.setNombreProducto(producto.getNombreProducto());
            pedido.setPrecio(precio);
            pedido.setImgUrl(producto.getImgUrl());
            pedido.setAnchoPrenda(Double.parseDouble(txtAncho.getText().toString()));
            pedido.setLargoPrenda(Double.parseDouble(txtLargo.getText().toString()));
            pedido.setCantidad(mQuantity);
            pedido.setTotal(mTotalPrice);

            pedidosDatabase.child(idPedido).setValue(pedido);

            obtenerPedidos();

            Toast.makeText(getContext(), "Producto comprado correctamente",
                    Toast.LENGTH_LONG).show();

        }else {
            Log.i(TAG , "Sin usuario activo");
            Toast.makeText(getContext(), "Necesitas iniciar sesión para comprar.",
                    Toast.LENGTH_LONG).show();

        }
    }

    private void updateNotificationsBadge(int count) {
        if (null != mListener) {
            mListener.updateNotificationsBadge(count);
        }
    }

    private void obtenerPedidos() {
        final int[] count = new int[1];

        if(clienteID != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference pedidosDatabase = database.getReference("pedidos");
            Query pedidosSearchQuery;

            pedidosSearchQuery = pedidosDatabase.orderByChild("idCliente").equalTo(clienteID);

            pedidosSearchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count[0] = (int) dataSnapshot.getChildrenCount();
                    updateNotificationsBadge(count[0]);
                    Log.w("Datos:", Long.toString(dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //count = 0;
                }
            });
        }
    }

    private void verificarAnchoLargo() {
        txtAncho.setError(null);
        txtLargo.setError(null);

        String anchoPrenda = txtAncho.getText().toString();
        String largoPrenda = txtLargo.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(anchoPrenda)) {
            txtAncho.setError(getString(R.string.error_field_required));
            focusView = txtAncho;
            cancel = true;
        }

        if (TextUtils.isEmpty(largoPrenda)) {
            txtLargo.setError(getString(R.string.error_field_required));
            focusView = txtLargo;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            calcularPrecio();
        }
    }

    private void calcularPrecio(){

        BigDecimal anchoPrenda = new BigDecimal(txtAncho.getText().toString());
        BigDecimal largoPrenda = new BigDecimal(txtLargo.getText().toString());
        int pedidos = mQuantity;

        PrecioVentaContent pvc = new PrecioVentaContent();
        BigDecimal costoUnitario = pvc.obtenerCostoUnitarioMateriaPrima(producto, anchoPrenda, largoPrenda, pedidos);

        precio = Double.parseDouble(String.valueOf(costoUnitario));

        displayPrecio(precio);

    }

    public void increment(){
        mQuantity = mQuantity + 1;
        verificarAnchoLargo();
        displayQuantity(mQuantity);
        mTotalPrice = mQuantity * precio;
        displayCost(mTotalPrice);
    }

    public void decrement(){
        if (mQuantity > 1){
            mQuantity = mQuantity - 1;
            verificarAnchoLargo();
            displayQuantity(mQuantity);
            mTotalPrice = mQuantity * precio;
            displayCost(mTotalPrice);
        }
    }

    private void displayQuantity(int numberOfItems) {
        TextView quantityTextView = (TextView) view.findViewById(R.id.tvCantidad);
        quantityTextView.setText(String.valueOf(numberOfItems));
    }

    private void displayPrecio(Double precio) {
        String convertPrice = NumberFormat.getCurrencyInstance().format(precio);
        tvPrecio.setText(convertPrice);
    }

    private void displayCost(Double totalPrice) {
        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        tvCostoTotal.setText(convertPrice);
    }

    @Override
    public void onPause(){
        super.onPause();
        obtenerPedidos();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
