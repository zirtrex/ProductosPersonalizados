package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;


import java.util.ArrayList;


public class ProveedorProductoMateriaPrimaRecyclerAdapter
        extends RecyclerView.Adapter<ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder> {

    private final OnProveedorFragmentInteractionListener mListener;

    static private ArrayList<MateriaPrimaPojo> lProductoMateriaPrima = new ArrayList<>();;
    private Context context;

    public ProveedorProductoMateriaPrimaRecyclerAdapter(Context context, ArrayList<MateriaPrimaPojo> materiasPrimas, OnProveedorFragmentInteractionListener listener){
        this.lProductoMateriaPrima = materiasPrimas;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proveedor_card_view_producto, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder viewHolder, int position) {

        final MateriaPrimaPojo materiaPrima = lProductoMateriaPrima.get(position);

        if(viewHolder.tvNombreMateriaPrima != null){
            viewHolder.itemView.setTag(materiaPrima.getNombreMateriaPrima()); //Este Tag es para poder eliminarlo
            viewHolder.tvNombreMateriaPrima.setText(materiaPrima.getNombreMateriaPrima());
        }

        if(viewHolder.tvValorMateriaPrima != null){
            viewHolder.tvValorMateriaPrima.setText(String.valueOf(materiaPrima.getValorMateriaPrima()));
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public MateriaPrimaPojo getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        }

        return lProductoMateriaPrima.get(position);
    }

    @Override
    public int getItemCount() {
        return lProductoMateriaPrima.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNombreMateriaPrima, tvValorMateriaPrima;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNombreMateriaPrima = (TextView) itemView.findViewById(R.id.tvNombre);
            tvValorMateriaPrima = (TextView) itemView.findViewById(R.id.tvValor);

        }
    }

}
