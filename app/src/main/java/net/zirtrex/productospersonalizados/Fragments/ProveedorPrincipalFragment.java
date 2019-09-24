package net.zirtrex.productospersonalizados.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.zirtrex.productospersonalizados.Activities.ProveedorActivity;
import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;


public class ProveedorPrincipalFragment extends Fragment {

    public static final String TAG = "ProveedorPrincipalFragment";

    private OnProveedorFragmentInteractionListener mListener;

    ProveedorActivity parentActivity;

    ImageButton imgBtnAgregarProducto;

    private View view;

    public ProveedorPrincipalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProveedorFragmentInteractionListener) {
            mListener = (OnProveedorFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " La actividad necesita implementar OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_principal, container, false);

        getActivity().setTitle(getText(R.string.title_proveedor_fragment_principal));

        parentActivity = (ProveedorActivity) getActivity();

        if(view != null){
            imgBtnAgregarProducto = (ImageButton) view.findViewById(R.id.imgBtnAgregarProducto);
            imgBtnAgregarProducto.setOnClickListener(agregarProducto);
        }

        return view;
    }

    View.OnClickListener agregarProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_proveedor, new ProveedorAgregarProductoFragment(), ProveedorAgregarProductoFragment.TAG)
                .addToBackStack(null)
                .commit();
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
