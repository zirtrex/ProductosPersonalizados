package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Models.Cart;
import net.zirtrex.productospersonalizados.Models.Pedidos;

import java.text.DecimalFormat;
import java.util.List;


public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.ViewHolder> {

    private Context mContext;
    private List<Pedidos> lPedidos;

    public PedidosAdapter(Context mContext, List<Pedidos> lPedidos) {
        this.mContext = mContext;
        this.lPedidos = lPedidos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pedidos_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Pedidos pedido = lPedidos.get(position);

        DecimalFormat precision = new DecimalFormat("0.00");

        viewHolder.itemView.setTag(pedido.getIdPedido());
        viewHolder.tvNombreProducto.setText(pedido.getNombreProducto());
        viewHolder.tvAncho.setText(pedido.getAnchoPrenda().toString());
        viewHolder.tvLargo.setText(pedido.getLargoPrenda().toString());
        viewHolder.tvCantidad.setText("Cantidad: " + String.valueOf(pedido.getCantidad()));
        viewHolder.tvPrecio.setText("S/ " + precision.format(pedido.getPrecio()));

        String imgUrl = pedido.getImgUrl();

        Glide.with(mContext.getApplicationContext())
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.load)
                .into(viewHolder.ivImagenProducto);

    }

    @Override
    public int getItemCount() {
        return lPedidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreProducto, tvAncho, tvLargo, tvCantidad, tvPrecio;
        ImageView ivImagenProducto;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvAncho = (TextView) itemView.findViewById(R.id.tvAnchoPrenda);
            tvLargo = (TextView) itemView.findViewById(R.id.tvLargoPrenda);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidad);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            ivImagenProducto = (ImageView) itemView.findViewById(R.id.ivImagenProducto);
        }

    }

}
