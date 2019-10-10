package net.zirtrex.productospersonalizados.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PrecioVentaContent {

    public static final String TAG = "PrecioVentaContent";

    List<MateriaPrimaAutocomplete> lMateriaPrima = new ArrayList<>();
    List<MaterialIndirectoAutocomplete> lMaterialIndirecto = new ArrayList<>();

    Producto producto;

    public PrecioVentaContent(Producto producto, Double anchoPrenda, Double largoPrenda, int pedidos) {
        this.producto = producto;

        if(producto != null){
            Double anchoTela = producto.getMateriaPrima().get("anchoTela");
            Double largoTela = calcularLargoTela(anchoTela, anchoPrenda, largoPrenda, pedidos);

            Log.w(TAG , String.valueOf(largoTela));
        }
    }


    /**
     * @param anchoTela en centímetros
     * @param anchoPrenda en centímetros
     * @param largoPrenda en centímetros
     * @param pedidos entero mínimo 1
     * @return largo de tela para los demás cálculos
     */
    private double calcularLargoTela(Double anchoTela, Double anchoPrenda, Double largoPrenda, int pedidos) {
        Double encajarAncho = (anchoPrenda * 2 + 10);
        Double encajarLargo = (largoPrenda + 6 + 12);

        Double prendasALoAncho = 0.0;
        if ((anchoTela - (encajarAncho * 2)) >= 0){
            prendasALoAncho = 2.0;
        }else if((anchoTela - encajarAncho) >= 0){
            prendasALoAncho = 1.0;
        }else if (anchoTela - (encajarAncho / 2) >= 0){
            prendasALoAncho = 0.5;
        }

        double tizadoLargo = 0;

        if(prendasALoAncho == 2) {
            if(pedidos > 2){
                tizadoLargo = (int) (encajarLargo * (pedidos * 0.5));
            }else{
                tizadoLargo = (int) (encajarLargo * (pedidos * 1));
            }
        }else if(prendasALoAncho == 1){
            tizadoLargo = (int) (encajarLargo * (pedidos * 1));
        }else if(prendasALoAncho == 0.5){
            tizadoLargo = (int) (encajarLargo * (pedidos * 2));
        }

        return  tizadoLargo;
    }

    /**
     * @param largoTela en metros
     * @param punta en metros
     * @param pedidos en entero
     * @return (m/pda) Consumo lineal en base al número de prendas inicialmente 3, será un usado en la posteriores fórmulas
     */
    private Double calcularConsumoLineal(Double largoTela, Double punta, int pedidos ) {
        return (largoTela + (2 * punta)) / pedidos;
    }

    /**
     * @param consumoLineal en m2/pda
     * @param anchoTela metros
     * @param orillo metros
     * @return (m2/pda)Área de prenda que será usada para calcular peso de la prenda
     */
    private Double calcularAreaPrenda(Double consumoLineal, Double anchoTela, Double orillo){
        return consumoLineal * (anchoTela + 2 * orillo);
    }

    /**
     * @param areaPrenda en m2/pda
     * @param densidadTela en gr/m2
     * @return (gr/pda) Devuelve el peso de la prenda
     */
    private Double calcularPesoPrenda(Double areaPrenda, Double densidadTela){
        return areaPrenda * densidadTela;
    }

    /**
     * @param pesoPrenda en gramos
     * @return Peso prenda en Kg
     */
    private Double calcularPesoPrendaKg(Double pesoPrenda){
        return pesoPrenda / 1000;
    }

    /**
     * @param consumoLineal en m/pda
     * @param pesoPrendaKg en Kilogramos
     * @return (m/kg)
     */
    private Double calcularRendimiento(Double consumoLineal, Double pesoPrendaKg){
        return  consumoLineal / pesoPrendaKg;
    }

    /**
     * @param pesoPrendaKg en kilogramos
     * @param pedidos en unidades
     * @param mermaCorte número será convertido a porcentaje
     * @param cantidadPrenda número será convertido a porcentaje
     * @return (kg) Necesario para calcular el Consumo Unitario
     */
    private Double calcularRequerimientoTela(Double pesoPrendaKg, Double pedidos, Double mermaCorte, Double cantidadPrenda){
        return (pesoPrendaKg * pedidos) / (1 - (mermaCorte/100)) * (1 - (cantidadPrenda/100));
    }

    /**
     * @param requerimientoTela en kilogramos
     * @param pedidos en enteros
     * @return (kg/pda)
     */
    private Double calcularConsumoUnitario(Double requerimientoTela, int pedidos){
        return requerimientoTela / pedidos;
    }

    /**
     * @param consumoUnitario (kg/pda)
     * @param precioTela (S./kg)
     * @return Costo unitario de materia prima
     */
    private Double calcularCostoUnitario(Double consumoUnitario, Double precioTela){
        return consumoUnitario * precioTela;
    }
    
    private Double calcularCostoInsumos(ArrayList<Double> insumos){
        Double total = 0.0;
        for(Double insumo : insumos){
            total += insumo;
        }

        return total;
    }

    private Double calcularMaterialesIndirectos(ArrayList<Double> materialesIndirectos){
        Double total = 0.0;
        for(Double mi : materialesIndirectos){
            total += mi;
        }

        return total;
    }

    private Double calcularGastosIndirectos(ArrayList<Double> gastosIndirectos){
        Double total = 0.0;
        for(Double gi : gastosIndirectos){
            total += gi;
        }

        return total;
    }

    private void populateLMateriaPrima() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        final Query materiaPrima;

        materiaPrima = myRef.child("materiaPrima");

        materiaPrima.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lMateriaPrima.removeAll(lMateriaPrima);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MateriaPrimaAutocomplete mP = new MateriaPrimaAutocomplete();
                    mP.setNombreMateriaPrima(postSnapshot.getKey());
                    mP.setDescripcionMateriaPrima(postSnapshot.getValue(String.class));
                    lMateriaPrima.add(mP);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void populateLMaterialesIndirectos() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef  = database.getReference();

        final Query materialesIndirectos;

        materialesIndirectos = myRef.child("materialesIndirectos");

        materialesIndirectos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lMaterialIndirecto.removeAll(lMaterialIndirecto);

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    MaterialIndirectoAutocomplete mP = new MaterialIndirectoAutocomplete();
                    mP.setNombreMaterialIndirecto(postSnapshot.getKey());
                    mP.setDescripcionMaterialIndirecto(postSnapshot.getValue(String.class));
                    lMaterialIndirecto.add(mP);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
