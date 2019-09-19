package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Fragments.ProductDetailFragment;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrima;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;
import net.zirtrex.productospersonalizados.Models.Productos;

import java.util.ArrayList;
import java.util.List;


public class ProveedorProductoMateriaPrimaRecyclerAdapter extends RecyclerView.Adapter<ProveedorProductoMateriaPrimaRecyclerAdapter.ViewHolder> {

    private final OnProveedorFragmentInteractionListener mListener;

    private static ArrayList<MateriaPrimaPojo> lProductoMateriaPrima;
    private Context context;


    public ProveedorProductoMateriaPrimaRecyclerAdapter(Context context, ArrayList<MateriaPrimaPojo> materiasPrimas, OnProveedorFragmentInteractionListener listener){
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

        viewHolder.txtNombreMateriaPrima.setText(materiaPrima.getNombreMateriaPrima());
        viewHolder.txtValorMateriaPrima.setText(String.valueOf(materiaPrima.getValorMateriaPrima()));

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

        public int currentItem;
        public EditText txtNombreMateriaPrima, txtValorMateriaPrima;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombreMateriaPrima = (EditText) itemView.findViewById(R.id.txtNombreMateriaPrima);
            txtValorMateriaPrima = (EditText) itemView.findViewById(R.id.txtValorMateriaPrima);

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
