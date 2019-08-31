package net.zirtrex.productospersonalizados.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.EfectivoTarjetaContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class EfectivoTarjetaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    View view;
    ArrayAdapter spnrTarjetaAdapter, spnrCuotasAdapter;

    public static List<String> lTarjetas = new ArrayList<>();
    public static List<Integer> lCuotas = new ArrayList<Integer>();

    Map<String, HashMap<Integer, Double>> hmTarjetas = new HashMap<String, HashMap<Integer, Double>>();

    Spinner spnrTarjetas, spnrCuotas;
    Button btnGenerarPago;
    EditText tvEfectivo;
    TextView tvConsumos, tvSubTotalConsumos, tvIVA, tvTotalConsumos,
            tvInteresFinanciamientoDiferido, tvTotal,
            tvFactor, tvResumenCuotas, tvInteresMensual;

    Double montoTotal = 0.00;
    String tarjetaSeleccionada;
    int nroCuotasSeleccionado;

    public EfectivoTarjetaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_efectivo_tarjeta, container, false);

        spnrTarjetas = (Spinner) view.findViewById(R.id.spnrTarjetas);
        spnrCuotas = (Spinner) view.findViewById(R.id.spnrCuotas);

        getCart();

        spnrTarjetaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, lTarjetas);
        spnrTarjetaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrTarjetas.setAdapter(spnrTarjetaAdapter);

        spnrCuotasAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, lCuotas);
        spnrCuotasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrCuotas.setAdapter(spnrCuotasAdapter);

        btnGenerarPago = (Button) view.findViewById(R.id.btnGenerarPago);
        tvEfectivo = (EditText) view.findViewById(R.id.tvEfectivo);
        tvConsumos = (TextView) view.findViewById(R.id.tvConsumos);
        tvSubTotalConsumos = (TextView) view.findViewById(R.id.tvSubTotalConsumos);
        tvIVA = (TextView) view.findViewById(R.id.tvIVA);
        tvTotalConsumos = (TextView) view.findViewById(R.id.tvTotalConsumos);
        tvInteresFinanciamientoDiferido = (TextView) view.findViewById(R.id.tvInteresFinanciamientoDiferido);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvFactor = (TextView) view.findViewById(R.id.tvFactor);
        tvResumenCuotas = (TextView) view.findViewById(R.id.tvResumenCuotas);
        tvInteresMensual = (TextView) view.findViewById(R.id.tvInteresMensual);

        btnGenerarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(spnrTarjetas.getSelectedItem() != null && spnrCuotas.getSelectedItem() != null){

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

            }
        });

        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            this.montoTotal = mListener.getMonto();
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
