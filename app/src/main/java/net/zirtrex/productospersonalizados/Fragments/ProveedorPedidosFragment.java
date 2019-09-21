package net.zirtrex.productospersonalizados.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;


public class ProveedorPedidosFragment extends Fragment {

    private OnProveedorFragmentInteractionListener mListener;

    View view;
    TextView tvMontoTotal;
    Double montoTotal = 0.00;

    public ProveedorPedidosFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_pedidos, container, false);

        getActivity().setTitle(getText(R.string.title_fragment_financiamiento));

        if(getArguments() != null) {
            montoTotal = getArguments().getDouble("montoTotal");
        }

        /*tvMontoTotal = (TextView) view.findViewById(R.id.tvMontoTotal);
        String convertPrice = NumberFormat.getCurrencyInstance().format(montoTotal);
        tvMontoTotal.setText("El monto de compra total es: " + convertPrice);


        //PrecioVentaContent ic = new PrecioVentaContent(montoTotal);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvFinanciamientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.setAdapter(new InversionAdapter(PrecioVentaContent.INVERSIONES, mListener));*/

        return view;
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
