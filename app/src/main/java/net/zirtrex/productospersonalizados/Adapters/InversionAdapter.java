package net.zirtrex.productospersonalizados.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Interfaces.OnFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.PrecioVentaContent.Inversiones;

import java.util.List;

import net.zirtrex.productospersonalizados.Activities.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Inversiones} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class InversionAdapter extends RecyclerView.Adapter<InversionAdapter.ViewHolder> {

    private final List<Inversiones> mValues;
    private final OnFragmentInteractionListener mListener;

    public InversionAdapter(List<Inversiones> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.proveedor_item_list_pedidos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvNroCuotas.setText(mValues.get(position).nroCuotas);
        holder.tvTexto.setText(mValues.get(position).texto);
        holder.tvMonto.setText(mValues.get(position).monto);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.saveMonto(0.00);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvNroCuotas;
        public final TextView tvTexto;
        public final TextView tvMonto;
        public Inversiones mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvNroCuotas = (TextView) view.findViewById(R.id.tvNroCuotas);
            tvTexto = (TextView) view.findViewById(R.id.tvTexto);
            tvMonto = (TextView) view.findViewById(R.id.tvMonto);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvTexto.getText() + "'";
        }
    }
}
