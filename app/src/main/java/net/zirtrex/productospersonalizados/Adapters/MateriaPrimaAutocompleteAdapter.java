package net.zirtrex.productospersonalizados.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import net.zirtrex.productospersonalizados.Activities.R;
import net.zirtrex.productospersonalizados.Models.MateriaPrimaAutocomplete;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class MateriaPrimaAutocompleteAdapter extends ArrayAdapter<MateriaPrimaAutocomplete> {


    private Context context;
    private List<MateriaPrimaAutocomplete> fullValues = new ArrayList<>();

    public MateriaPrimaAutocompleteAdapter(Context context, List<MateriaPrimaAutocomplete> values) {
        super(context, 0, values);
        this.context = context;
        this.fullValues = values;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return materiaPrimaFilter;
    }

    private Filter materiaPrimaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<MateriaPrimaAutocomplete> sugerencias = new ArrayList<>();
            String substr = constraint.toString().toLowerCase();
            if(substr == null || substr.length() == 0){
                results.values = fullValues;
                results.count = fullValues.size();
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(MateriaPrimaAutocomplete mp : fullValues){
                    if(mp.getDescripcionMateriaPrima().toLowerCase().contains(filterPattern) ){
                        sugerencias.add(mp);
                    }
                }

                results.values = sugerencias;
                results.count = sugerencias.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<MateriaPrimaAutocomplete> filteredList = (List<MateriaPrimaAutocomplete>) results.values;
            if(results != null && results.count > 0) {
                clear();
                addAll(filteredList);
                notifyDataSetChanged();
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((MateriaPrimaAutocomplete) resultValue).getDescripcionMateriaPrima();
        }
    };

    @Override
    public int getCount(){
        return fullValues.size();
    }

    @Override
    public MateriaPrimaAutocomplete getItem(int position){
        return fullValues.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.autocomplete_fila_materia_prima, parent, false
            );
        }

        TextView tvDescripcionMateriaPrima = convertView.findViewById(R.id.tvSpinnerFila);

        MateriaPrimaAutocomplete mpa = getItem(position);

        if (mpa != null){
            tvDescripcionMateriaPrima.setText(mpa.getDescripcionMateriaPrima());
        }

        return convertView;
    }

}
