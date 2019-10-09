package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaAutocomplete;
import net.zirtrex.productospersonalizados.Models.MaterialIndirectoAutocomplete;

import java.util.List;

public class MaterialIndirectoSpinnerAdapter extends ArrayAdapter<MaterialIndirectoAutocomplete> {

    private LayoutInflater mInflater;
    private List<MaterialIndirectoAutocomplete> values;

    public MaterialIndirectoSpinnerAdapter(Context context, int resouceId, int textviewId, List<MaterialIndirectoAutocomplete> inValues) {
        super(context, resouceId, textviewId, inValues);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return rowview(position, convertView, parent);
    }

    private View rowview(int position, View convertView, ViewGroup parent){

        MaterialIndirectoAutocomplete rowItem = getItem(position);

        SpinnerViewHolder holder;
        View rowview = convertView;

        if (rowview == null) {

            holder = new SpinnerViewHolder();
            mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = mInflater.inflate(R.layout.proveedor_spinner_fila, null, false);

            holder.tvDescripcionMaterialIndirecto = (TextView) rowview.findViewById(R.id.tvSpinnerFila);
            rowview.setTag(holder);
        }else{
            holder = (SpinnerViewHolder) rowview.getTag();
        }
        holder.tvDescripcionMaterialIndirecto.setText(rowItem.getDescripcionMaterialIndirecto());

        return rowview;
    }

    private class SpinnerViewHolder{
        TextView tvDescripcionMaterialIndirecto;
    }

}
