package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaAutocomplete;

import java.util.List;

public class MateriaPrimaSpinnerAdapter extends ArrayAdapter<MateriaPrimaAutocomplete> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<MateriaPrimaAutocomplete> values;
    //private final int mResource;

    public MateriaPrimaSpinnerAdapter(Context context, int resouceId, int textviewId, List<MateriaPrimaAutocomplete> inValues) {
        super(context, resouceId, textviewId, inValues);
        //this.mContext = context;
        //mInflater = LayoutInflater.from(context);
        //values = inValues;
    }

    /*@Override
    public int getCount(){
        return values.size();
    }

    @Override
    public MateriaPrimaAutocomplete getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*View view = mInflater.inflate(R.layout.proveedor_spinner_fila, null, true);

        MateriaPrimaAutocomplete currentMateriaPrima = values.get(position);

        TextView tvDescripcionMateriaPrima = (TextView) view.findViewById(R.id.tvDescripcionMateriaPrima);
        tvDescripcionMateriaPrima.setText(currentMateriaPrima.getDescripcionMateriaPrima());

        return view;*/

        return rowview(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /*TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getDescripcionMateriaPrima());

        return label;

        View view = mInflater.inflate(R.layout.proveedor_spinner_fila, null, true);

        MateriaPrimaAutocomplete currentMateriaPrima = values.get(position);

        TextView tvDescripcionMateriaPrima = (TextView) view.findViewById(R.id.tvDescripcionMateriaPrima);
        tvDescripcionMateriaPrima.setText(currentMateriaPrima.getDescripcionMateriaPrima());

        return view;*/

        return rowview(position, convertView, parent);
    }

    private View rowview(int position, View convertView, ViewGroup parent){

        MateriaPrimaAutocomplete rowItem = getItem(position);

        SpinnerViewHolder holder;
        View rowview = convertView;

        if (rowview == null) {

            holder = new SpinnerViewHolder();
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = mInflater.inflate(R.layout.proveedor_spinner_fila, null, false);

            holder.tvDescripcionMateriaPrima = (TextView) rowview.findViewById(R.id.tvSpinnerFila);
            rowview.setTag(holder);
        }else{
            holder = (SpinnerViewHolder) rowview.getTag();
        }
        holder.tvDescripcionMateriaPrima.setText(rowItem.getDescripcionMateriaPrima());

        return rowview;
    }

    private class SpinnerViewHolder{
        TextView tvDescripcionMateriaPrima;
    }

}
