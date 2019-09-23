package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;

import java.util.List;


public class ProveedorProductoMateriaPrimaRecyclerAdapter extends RecyclerView.Adapter<ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder> {

    private final OnProveedorFragmentInteractionListener mListener;

    private List<MateriaPrimaPojo> lProductoMateriaPrima;
    private Context context;


    public ProveedorProductoMateriaPrimaRecyclerAdapter(Context context, List<MateriaPrimaPojo> materiasPrimas, OnProveedorFragmentInteractionListener listener){
        this.lProductoMateriaPrima = materiasPrimas;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proveedor_card_view_producto_materia_prima, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder viewHolder, int position) {

        MateriaPrimaPojo materiaPrima = lProductoMateriaPrima.get(position);

        if(viewHolder.txtNombreMateriaPrima != null){
            viewHolder.txtNombreMateriaPrima.setText(materiaPrima.getNombreMateriaPrima());
        }

        if(viewHolder.txtValorMateriaPrima != null){
            viewHolder.txtValorMateriaPrima.setText(String.valueOf(materiaPrima.getValorMateriaPrima()));
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

        public EditText txtNombreMateriaPrima, txtValorMateriaPrima;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreMateriaPrima = (EditText) itemView.findViewById(R.id.txtNombreMateriaPrima);
            txtValorMateriaPrima = (EditText) itemView.findViewById(R.id.txtValorMateriaPrima);

            if(txtNombreMateriaPrima != null) {

                txtNombreMateriaPrima.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        MateriaPrimaPojo materiaPrimaItem = lProductoMateriaPrima.get(getAdapterPosition());
                        materiaPrimaItem.setNombreMateriaPrima(charSequence + "");
                        lProductoMateriaPrima.set(getAdapterPosition(), materiaPrimaItem);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
            if(txtNombreMateriaPrima != null) {
                txtValorMateriaPrima.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        MateriaPrimaPojo materiaPrimaItem = lProductoMateriaPrima.get(getAdapterPosition());

                        try {
                            materiaPrimaItem.setValorMateriaPrima(Double.parseDouble(charSequence + ""));
                        }
                        catch (NumberFormatException e) {
                            materiaPrimaItem.setValorMateriaPrima(0.00);
                        }

                        lProductoMateriaPrima.set(getAdapterPosition(), materiaPrimaItem);

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }


            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){

                        // Creamos un nuevo Bundle para pasar el monto
                        Bundle args = new Bundle();
                        // Colocamos el monto total
                        args.putString("nombreProducto", getItem(position).getNombreProducto());
                        args.putString("imgUrl", getItem(position).getImgUrl());
                        args.putDouble("precio", getItem(position).getPrecio());

                        Fragment fProductDetailFragment = new ProductDetailFragment();
                        fProductDetailFragment.setArguments(args);
                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_cliente, fProductDetailFragment,"Fragment Product Detail")
                                .addToBackStack(null)
                                .commit();

                        Snackbar.make( v, "Click detected on item " + position,
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }

                }
            });*/
        }
    }
}
