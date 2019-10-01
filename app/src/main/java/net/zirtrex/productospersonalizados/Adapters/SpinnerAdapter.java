package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.zirtrex.productospersonalizados.Models.MateriaPrimaPojo;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<MateriaPrimaPojo> {


    private Context context;
    // Your custom values for the spinner (User)
    private List<MateriaPrimaPojo> values;

    public SpinnerAdapter(Context context, int textViewResourceId, List<MateriaPrimaPojo> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public MateriaPrimaPojo getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(String.valueOf(values.get(position).getValorMateriaPrima()));

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(String.valueOf(values.get(position).getValorMateriaPrima()));

        return label;
    }

}
