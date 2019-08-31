package net.zirtrex.productospersonalizados.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.zirtrex.productospersonalizados.Activities.R;


public class ChequeFragment extends Fragment {


    public ChequeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cheque, container, false);
    }

}
