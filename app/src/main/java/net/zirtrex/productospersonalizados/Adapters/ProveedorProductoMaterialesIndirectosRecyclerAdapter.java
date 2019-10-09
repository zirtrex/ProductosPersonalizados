package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Interfaces.OnProveedorFragmentInteractionListener;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;
import net.zirtrex.productospersonalizados.Models.MaterialesIndirectosPojo;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class ProveedorProductoMaterialesIndirectosRecyclerAdapter
        extends RecyclerView.Adapter<ProveedorProductoMaterialesIndirectosRecyclerAdapter.ViewHolder> {

    private final OnProveedorFragmentInteractionListener mListener;

    static private ArrayList<MaterialesIndirectosPojo> lProductoMaterialesIndirectos = new ArrayList<>();;
    private Context context;

    public ProveedorProductoMaterialesIndirectosRecyclerAdapter(Context context, ArrayList<MaterialesIndirectosPojo> materiasPrimas, OnProveedorFragmentInteractionListener listener){
        this.lProductoMaterialesIndirectos = materiasPrimas;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ProveedorProductoMaterialesIndirectosRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.proveedor_card_view_producto, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ProveedorProductoMaterialesIndirectosRecyclerAdapter.ViewHolder viewHolder, int position) {

        final MaterialesIndirectosPojo materiaPrima = lProductoMaterialesIndirectos.get(position);

        if(viewHolder.tvNombre != null){
            viewHolder.itemView.setTag(materiaPrima.getNombreMaterialIndirecto()); //Este Tag es para poder eliminarlo
            viewHolder.tvNombre.setText(materiaPrima.getNombreMaterialIndirecto());
        }

        if(viewHolder.tvValor != null){
            viewHolder.tvValor.setText(String.valueOf(materiaPrima.getValorMaterialIndirecto()));
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public MaterialesIndirectosPojo getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        }

        return lProductoMaterialesIndirectos.get(position);
    }

    @Override
    public int getItemCount() {
        return lProductoMaterialesIndirectos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNombre, tvValor;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            tvValor = (TextView) itemView.findViewById(R.id.tvValor);

        }
    }

}
