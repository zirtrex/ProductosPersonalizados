package net.zirtrex.productospersonalizados.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;


public class ProveedorPrincipalFragment extends Fragment {

    public static final String TAG = "ProveedorPrincipalFragment";

    private OnProveedorFragmentInteractionListener mListener;

    ImageButton imgBtnAgregarProducto;

    private View view;

    public ProveedorPrincipalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_principal, container, false);

        getActivity().setTitle(getText(R.string.title_proveedor_fragment_principal));

        imgBtnAgregarProducto = (ImageButton) view.findViewById(R.id.imgBtnAgregarProducto);

        imgBtnAgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_proveedor, new ProveedorAgregarProductoFragment(), ProveedorAgregarProductoFragment.TAG)
                        .commit();
            }
        });

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
