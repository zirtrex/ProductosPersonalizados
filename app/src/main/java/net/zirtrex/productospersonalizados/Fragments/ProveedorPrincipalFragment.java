package net.zirtrex.productospersonalizados.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

    ImageButton imgBtnAgregarProducto, imgBtnVerProductos, imgBtnVerPedidos;

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
                    + " Se debe implementar OnProveedorFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.proveedor_fragment_principal, container, false);

        //getActivity().setTitle(getText(R.string.title_proveedor_fragment_principal));

        parentActivity = (ProveedorActivity) getActivity();

        if(view != null){
            //Botones
            imgBtnAgregarProducto = (ImageButton) view.findViewById(R.id.imgBtnAgregarProducto);
            imgBtnVerProductos = (ImageButton) view.findViewById(R.id.imgBtnVerProductos);
            imgBtnVerPedidos = (ImageButton) view.findViewById(R.id.imgBtnVerPedidos);

            //Acciones para los botones
            imgBtnAgregarProducto.setOnClickListener(agregarProducto);
            imgBtnVerProductos.setOnClickListener(verProductos);
            imgBtnVerPedidos.setOnClickListener(verPedidos);
        }

        return view;
    }

    View.OnClickListener agregarProducto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.nav_proveedor_agregar_producto);
        }
    };

    View.OnClickListener verProductos = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.nav_proveedor_productos);
        }
    };

    View.OnClickListener verPedidos = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(R.id.nav_proveedor_pedidos);
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
