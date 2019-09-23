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

import java.text.DecimalFormat;
import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Context mContext;
    private List<Cart> lCart;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public CartAdapter(Context mContext, List<Cart> carts) {
        this.mContext = mContext;
        this.lCart = carts;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CartViewHolder viewHolder, int position) {

        Cart cart = lCart.get(position);

        DecimalFormat precision = new DecimalFormat("0.00");

        //Set values
        viewHolder.itemView.setTag(cart.getCartId());
        viewHolder.tvNombreProducto.setText(cart.getCartNombreProducto());
        viewHolder.tvCantidad.setText("Quantity ordering: " + String.valueOf(cart.getCartCantidad()));
        viewHolder.tvPrecio.setText("$" + precision.format(cart.getCartPrecioTotal()));

        String poster = cart.getCartImgUrl();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(viewHolder.ivImagenProducto);

    }

    @Override
    public int getItemCount() {

        return lCart.size();

    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreProducto, tvCantidad, tvPrecio;
        ImageView ivImagenProducto;

        public CartViewHolder(View itemView) {
            super(itemView);

            tvNombreProducto = (TextView) itemView.findViewById(R.id.tvCartNombreProducto);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCartCantidad);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvCartPrecio);
            ivImagenProducto = (ImageView) itemView.findViewById(R.id.ivCartImagenProducto);
        }

    }

}
