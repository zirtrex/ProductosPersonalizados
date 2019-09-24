package net.zirtrex.productospersonalizados.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import net.zirtrex.productospersonalizados.Adapters.ProveedorProductoMateriaPrimaRecyclerAdapter;
import net.zirtrex.productospersonalizados.Adapters.SpinnerAdapter;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrima;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;
import net.zirtrex.productospersonalizados.Models.Productos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ProveedorAgregarProductoFragment extends Fragment {

    public static final String TAG = "AgregarProductoFragment";

    private OnProveedorFragmentInteractionListener mListener;

    ArrayAdapter spnrTarjetaAdapter, spnrCuotasAdapter;

    public static List<String> lTarjetas = new ArrayList<>();
    public static List<Integer> lCuotas = new ArrayList<Integer>();

    Map<String, HashMap<Integer, Double>> hmTarjetas = new HashMap<String, HashMap<Integer, Double>>();

    RadioGroup rgTipoPrenda;
    RadioButton radioButton;

    RecyclerView rvProductoMateriaPrima;
    ProveedorProductoMateriaPrimaRecyclerAdapter proveedorProductoMateriaPrimaRA;
    static List<MateriaPrimaPojo> lProductoMateriaPrima = new ArrayList<>();

    SpinnerAdapter spAdap;
    Spinner spnrTarjetas, spnrCuotas;
    Button btnAgregarMateriaPrima, btnAgregarMaterialesIndirectos, btnGuardarProducto;
    EditText txtGastosFinancieros, txtImgUrl, txtNombreProducto;
    TextView tvSeleccionTipoPrenda;
    View view;

    Double montoTotal = 0.00;
    String tarjetaSeleccionada;
    int nroCuotasSeleccionado;

    public ProveedorAgregarProductoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.proveedor_fragment_agregar_producto, container, false);

        rgTipoPrenda = (RadioGroup) view.findViewById(R.id.rgTipoPrenda);
        tvSeleccionTipoPrenda = (TextView) view.findViewById(R.id.tvSeleccionTipoPrenda);

        rgTipoPrenda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbPolos:
                        tvSeleccionTipoPrenda.setText("Has elegido: Polos" );
                        break;
                    case R.id.rbPantalones:
                        tvSeleccionTipoPrenda.setText("Has elegido: Pantalones" );
                        break;
                    case R.id.rbZapatos:
                        tvSeleccionTipoPrenda.setText("Has elegido: Zapatos" );
                        break;
                }
            }
        });

        rvProductoMateriaPrima = (RecyclerView) view.findViewById(R.id.rvProductoMateriaPrima);

        proveedorProductoMateriaPrimaRA = new ProveedorProductoMateriaPrimaRecyclerAdapter(getContext(), lProductoMateriaPrima, mListener);
        rvProductoMateriaPrima.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProductoMateriaPrima.setAdapter(proveedorProductoMateriaPrimaRA);

        spnrTarjetas = (Spinner) view.findViewById(R.id.spnrTarjetas);
        spnrCuotas = (Spinner) view.findViewById(R.id.spnrCuotas);

        //getCart();

        MateriaPrimaPojo[] mp = new MateriaPrimaPojo[2];

        mp[0] = new MateriaPrimaPojo();
        mp[0].setNombreMateriaPrima("materia1");
        mp[0].setValorMateriaPrima(0.003);
        mp[1] = new MateriaPrimaPojo();
        mp[1].setNombreMateriaPrima("materia2");
        mp[1].setValorMateriaPrima(0.05);

        spAdap = new SpinnerAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, mp);

        //pnrTarjetaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, lTarjetas);
        //spnrTarjetaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrTarjetas.setAdapter(spAdap);

        spnrTarjetas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                MateriaPrimaPojo mp = spAdap.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(getActivity(), "ID: " + mp.getNombreMateriaPrima() + "\nName: " + mp.getValorMateriaPrima(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        /*spnrCuotasAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, lCuotas);
        spnrCuotasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrCuotas.setAdapter(spnrCuotasAdapter);*/

        //Botones
        btnAgregarMateriaPrima = (Button) view.findViewById(R.id.btnAgregarMateriaPrima);
        btnAgregarMateriaPrima = (Button) view.findViewById(R.id.btnAgregarMateriaPrima);
        btnGuardarProducto = (Button) view.findViewById(R.id.btnGuardarProducto);
        //Cajas de texto
        txtGastosFinancieros = (EditText) view.findViewById(R.id.txtGastosFinancieros);
        txtImgUrl = (EditText) view.findViewById(R.id.txtImgUrl);
        txtNombreProducto = (EditText) view.findViewById(R.id.txtNombreProducto);

        btnAgregarMateriaPrima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MateriaPrimaPojo materiaPrimaItem = new MateriaPrimaPojo();
                materiaPrimaItem.setNombreMateriaPrima("Nombre Materia Prima");
                materiaPrimaItem.setValorMateriaPrima(0.00);
                lProductoMateriaPrima.add(materiaPrimaItem);
                proveedorProductoMateriaPrimaRA.notifyDataSetChanged();
            }

            /*if(spnrTarjetas.getSelectedItem() != null && spnrCuotas.getSelectedItem() != null){

                tarjetaSeleccionada = spnrTarjetas.getSelectedItem().toString();
                nroCuotasSeleccionado = Integer.parseInt(spnrCuotas.getSelectedItem().toString());

                EfectivoTarjetaContent etc;

                if(!TextUtils.isEmpty(tvEfectivo.getText())){
                    Double efectivo = Double.parseDouble(tvEfectivo.getText().toString());
                    Log.w("Monto Total", String.valueOf(montoTotal));
                    Toast.makeText(getActivity(),tarjetaSeleccionada + " - " + nroCuotasSeleccionado + " - " + efectivo, Toast.LENGTH_LONG).show();
                    etc = new EfectivoTarjetaContent(montoTotal, hmTarjetas, tarjetaSeleccionada, nroCuotasSeleccionado, efectivo);
                }else{
                    Log.w("Monto Total", String.valueOf(montoTotal));
                    etc = new EfectivoTarjetaContent(montoTotal, hmTarjetas, tarjetaSeleccionada, nroCuotasSeleccionado, 0.00);
                }

                tvConsumos.setText(etc.getTvConsumos());
                tvSubTotalConsumos.setText(etc.getTvSubTotalConsumos());
                tvIVA.setText(etc.getTvIVA());
                tvTotalConsumos.setText(etc.getTvTotalConsumos());
                tvInteresFinanciamientoDiferido.setText(etc.getTvInteresFinanciamientoDiferido());
                tvTotal.setText(etc.getTvTotal());
                tvFactor.setText(etc.getTvFactor());
                tvResumenCuotas.setText(etc.getTvResumenCuotas());
                tvInteresMensual.setText(etc.getTvInteresMensual());

            }else{
                Toast.makeText(getActivity(),"Seleccione Tarjeta y Nro de Cuotas.", Toast.LENGTH_LONG).show();
            }

            }*/
        });

        btnGuardarProducto.setOnClickListener(confirmarAgregarProducto);

        return view;
    }

    View.OnClickListener confirmarAgregarProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setMessage("¿Desea agregar el producto?");

            builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    guardarProducto();
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
    };

    private void guardarProducto() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference productosDatabase  = database.getReference("productos");

        Productos producto = new Productos();

        String idProducto = productosDatabase.push().getKey();

        producto.setIdProducto(idProducto);
        producto.setNombreProducto(txtNombreProducto.getText().toString());
        producto.setImgUrl(txtImgUrl.getText().toString());

        Map<String, Double> materiasPrima = new HashMap<>();

        for (int i = 0; i < lProductoMateriaPrima.size(); i++){
            MateriaPrimaPojo materiaPrimaActual = lProductoMateriaPrima.get(i);
            materiasPrima.put(materiaPrimaActual.getNombreMateriaPrima(), materiaPrimaActual.getValorMateriaPrima());
        }

        producto.setMateriaPrima(materiasPrima);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            producto.setIdUsuario(user.getUid());
            productosDatabase.child(idProducto).setValue(producto);
            Log.w(TAG , "Usuario Logueado");
            Toast.makeText(getContext(), "Producto agregado correctamente",
                    Toast.LENGTH_LONG).show();


        }else {
            Log.w(TAG , "Sin usuario activo");
            Toast.makeText(getContext(), "Necesitas iniciar sesión para guardar los datos.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void getCart() {

        final int[] count = new int[1];

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        final Query tarjetas;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            tarjetas = myRef.child("tarjetas");
        }else{
            tarjetas = myRef.child("tarjetas");
        }

        tarjetas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    HashMap<Integer, Double> cuotasPorcentajeInteres = (HashMap<Integer, Double>) postSnapshot.getValue();

                    hmTarjetas.put(postSnapshot.getKey(), cuotasPorcentajeInteres);

                    Log.w("Datos: ", postSnapshot.getKey());
                    Log.w("Datos: ", postSnapshot.getValue().toString());
                }
                populateLTarjetas();
                populateLCoutas();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //count = 0;
            }
        });
    }

    private void populateLTarjetas(){

        Iterator<Map.Entry<String, HashMap<Integer, Double>>> iTarjetas = hmTarjetas.entrySet().iterator();
        lTarjetas.removeAll(lTarjetas);

        while (iTarjetas.hasNext()){
            Map.Entry<String, HashMap<Integer,Double>> tarjeta = (Map.Entry) iTarjetas.next();
            lTarjetas.add(tarjeta.getKey());
            Log.w("Populate Tarjetas", tarjeta.getKey());
        }
        spnrTarjetaAdapter.notifyDataSetChanged();
    }

    private void populateLCoutas(){

        HashMap<Integer, Double> hmCoutas = hmTarjetas.get("Diners");

        Iterator<Map.Entry<Integer, Double>> iCuotas = hmCoutas.entrySet().iterator();
        lCuotas.removeAll(lCuotas);

        while (iCuotas.hasNext()){
            Map.Entry<Integer,Double> cuota = (Map.Entry) iCuotas.next();
            lCuotas.add(cuota.getKey());
            Log.w("Populate Cuotas", String.valueOf(cuota.getKey()));
        }
        Collections.sort(lCuotas);
        spnrCuotasAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
            //this.montoTotal = mListener.getMonto();
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
