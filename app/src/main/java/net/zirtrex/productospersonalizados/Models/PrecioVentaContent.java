package net.zirtrex.productospersonalizados.Models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class PrecioVentaContent {

    public static final String TAG = "PrecioVentaContent";

    List<MateriaPrimaAutocomplete> lMateriaPrima = new ArrayList<>();
    List<MaterialIndirectoAutocomplete> lMaterialIndirecto = new ArrayList<>();

    Producto producto;

    public PrecioVentaContent() {}

    public BigDecimal obtenerCostoUnitarioMateriaPrima(Producto producto, BigDecimal anchoPrenda, BigDecimal largoPrenda, int pedidos){
        if(producto != null){
            BigDecimal anchoTela = BigDecimal.valueOf(producto.getMateriaPrima().get("anchoTela"));
            BigDecimal punta = BigDecimal.valueOf(producto.getMateriaPrima().get("punta"));
            BigDecimal orillo = BigDecimal.valueOf(producto.getMateriaPrima().get("orillo"));
            BigDecimal mermaCorte = BigDecimal.valueOf(producto.getMateriaPrima().get("mermaCorte"));
            BigDecimal mermaSegunda = BigDecimal.valueOf(producto.getMateriaPrima().get("mermaSegunda"));
            BigDecimal cantidadPrenda = BigDecimal.valueOf(producto.getMateriaPrima().get("cantidadPrenda"));
            BigDecimal densidadTela = BigDecimal.valueOf(producto.getMateriaPrima().get("densidadTela"));
            BigDecimal precioTela = BigDecimal.valueOf(producto.getMateriaPrima().get("precioTela"));

            BigDecimal largoTela = calcularLargoTela(anchoTela, anchoPrenda, largoPrenda, pedidos);
            BigDecimal consumoLineal = calcularConsumoLineal(largoTela, punta, pedidos);
            BigDecimal areaPrenda = calcularAreaPrenda(consumoLineal, anchoTela, orillo);
            BigDecimal pesoPrenda = calcularPesoPrenda(areaPrenda, densidadTela);
            BigDecimal pesoPrendaKg = calcularPesoPrendaKg(pesoPrenda);
            BigDecimal rendimiento = calcularRendimiento(consumoLineal, pesoPrendaKg);
            BigDecimal requerimientoTela = calcularRequerimientoTela(pesoPrendaKg,pedidos, mermaCorte,cantidadPrenda);
            BigDecimal consumoUnitario = calcularConsumoUnitario(requerimientoTela, pedidos);
            BigDecimal costoUnitario = calcularCostoUnitario(consumoUnitario, precioTela);

            return costoUnitario;
        }
        return null;
    }

    /**
     * @param anchoTela en centímetros
     * @param anchoPrenda en centímetros
     * @param largoPrenda en centímetros
     * @param pedidos entero mínimo 1
     * @return (m)largo de tela para los demás cálculos
     */
    private BigDecimal calcularLargoTela(BigDecimal anchoTela, BigDecimal anchoPrenda, BigDecimal largoPrenda, int pedidos) {
        //Castear datos a BigDecinal
        BigDecimal anchoPrendaM = anchoPrenda.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);
        BigDecimal largoPrendaM = largoPrenda.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP);

        BigDecimal encajarAncho = anchoPrendaM.multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(0.10));
        BigDecimal encajarLargo = largoPrendaM.add(BigDecimal.valueOf(0.06).add(BigDecimal.valueOf(0.12)));
        BigDecimal pedidosBD = BigDecimal.valueOf(pedidos);

        BigDecimal prendasALoAncho = BigDecimal.valueOf(0);

        BigDecimal op1 = anchoTela.subtract(encajarAncho.multiply(BigDecimal.valueOf(2)));
        BigDecimal op2 = anchoTela.subtract(encajarAncho);
        BigDecimal op3 = anchoTela.subtract(encajarAncho.divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_UP));

        //Log.w(TAG , String.valueOf(op1));
        //Log.w(TAG , String.valueOf(op2));
        //Log.w(TAG , String.valueOf(op3));

        BigDecimal cero = BigDecimal.valueOf(0);

        BigDecimal tizadoLargo = BigDecimal.valueOf(0);

        if (op1.compareTo(cero) == 1){
            Log.w(TAG , "op1");
            prendasALoAncho = BigDecimal.valueOf(2.0);

            if(pedidos > 2){
                Log.w(TAG , "op11");
                tizadoLargo = encajarLargo.multiply(pedidosBD.multiply(BigDecimal.valueOf(0.5)));
            }else{
                Log.w(TAG , "op12");
                tizadoLargo = encajarLargo.multiply(pedidosBD.multiply(BigDecimal.valueOf(1)));
            }

        }else if(op2.compareTo(cero) == 1){
            Log.w(TAG , "op2");
            prendasALoAncho = BigDecimal.valueOf(1.0);

            tizadoLargo = encajarLargo.multiply(pedidosBD.multiply(BigDecimal.valueOf(1)));

        }else if (op3.compareTo(cero) == 1){
            Log.w(TAG , "op2");
            prendasALoAncho = BigDecimal.valueOf(0.5);

            tizadoLargo = encajarLargo.multiply(pedidosBD.multiply(BigDecimal.valueOf(2)));
        }

        Log.w(TAG , "largoTela: " + tizadoLargo);
        return  tizadoLargo;
    }

    /**
     * @param largoTela en metros
     * @param punta en metros
     * @param pedidos en entero
     * @return (cm/pda) Consumo lineal en base al número de prendas inicialmente 3, será un usado en la posteriores fórmulas
     */
    private BigDecimal calcularConsumoLineal(BigDecimal largoTela, BigDecimal punta, int pedidos ) {
        BigDecimal pedidosBD = BigDecimal.valueOf(pedidos);

        //Fórmula original (largoTela + (2 * punta)) / pedidos;
        BigDecimal consumoLineal = largoTela.add(BigDecimal.valueOf(2).multiply(punta)).divide(pedidosBD, 3, RoundingMode.HALF_UP);
        Log.w(TAG , "consumoLineal: " + String.valueOf(consumoLineal));
        return consumoLineal;
    }

    /**
     * @param consumoLineal en cm2/pda
     * @param anchoTela cm
     * @param orillo metros
     * @return (cm/pda)Área de prenda que será usada para calcular peso de la prenda
     */
    private BigDecimal calcularAreaPrenda(BigDecimal consumoLineal, BigDecimal anchoTela, BigDecimal orillo){
        //Fórmula original consumoLineal * (anchoTela + (2 * orillo));
        BigDecimal op1 = BigDecimal.valueOf(2).multiply(orillo);
        Log.w(TAG , "orillo: " + String.valueOf(op1));
        BigDecimal op2 = anchoTela.add(op1);
        BigDecimal areaPrenda = consumoLineal.multiply(op2);
        Log.w(TAG , "areaPrenda: " + String.valueOf(areaPrenda));
        return areaPrenda;
    }

    /**
     * @param areaPrenda en cm2/pda
     * @param densidadTela en gr/m2
     * @return (m/pda) Devuelve el peso de la prenda
     */
    private BigDecimal calcularPesoPrenda(BigDecimal areaPrenda, BigDecimal densidadTela){
        BigDecimal pesoPrenda = areaPrenda.multiply(densidadTela);
        Log.w(TAG , "pesoPrenda: " + String.valueOf(pesoPrenda));
        return pesoPrenda;
    }

    /**
     * @param pesoPrenda en gramos
     * @return Peso prenda en Kg
     */
    private BigDecimal calcularPesoPrendaKg(BigDecimal pesoPrenda){
        BigDecimal pesoPrendaKg = pesoPrenda.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        Log.w(TAG , "pesoPrendaKg: " + String.valueOf(pesoPrendaKg));
        return pesoPrendaKg;
    }

    /**
     * @param consumoLineal en cm/pda
     * @param pesoPrendaKg en Kilogramos
     * @return (m/kg)
     */
    private BigDecimal calcularRendimiento(BigDecimal consumoLineal, BigDecimal pesoPrendaKg){
        BigDecimal rendimiento = consumoLineal.divide(pesoPrendaKg, 3, RoundingMode.HALF_UP);
        return  rendimiento;
    }

    /**
     * @param pesoPrendaKg en kilogramos
     * @param pedidos en unidades
     * @param mermaCorte número será convertido a porcentaje
     * @param cantidadPrenda número será convertido a porcentaje
     * @return (kg) Necesario para calcular el Consumo Unitario
     */
    private BigDecimal calcularRequerimientoTela(BigDecimal pesoPrendaKg, int pedidos, BigDecimal mermaCorte, BigDecimal cantidadPrenda){
        BigDecimal pedidosBD = BigDecimal.valueOf(pedidos);

        BigDecimal mermaCortePor = mermaCorte.divide(BigDecimal.valueOf(100),3, RoundingMode.HALF_UP);
        BigDecimal cantidadPrendaPor = cantidadPrenda.divide(BigDecimal.valueOf(100),3, RoundingMode.HALF_UP);

        //Fórmula original (pesoPrendaKg * pedidos) / (1 - (mermaCorte/100)) * (1 - (cantidadPrenda/100));
        BigDecimal dividendo = pesoPrendaKg.multiply(pedidosBD);
        BigDecimal divisor = (BigDecimal.valueOf(1).subtract(mermaCortePor)).multiply((BigDecimal.valueOf(1).subtract(cantidadPrendaPor)));

        Log.w(TAG ,  String.valueOf("dividendo: " + dividendo));
        Log.w(TAG ,  String.valueOf("divisor: " + divisor));
        BigDecimal requerimientoTela = dividendo.divide(divisor, 3, RoundingMode.HALF_UP);
        Log.w(TAG ,  String.valueOf("requerimientoTela: " + requerimientoTela));
        return requerimientoTela;
    }

    /**
     * @param requerimientoTela en kilogramos
     * @param pedidos en enteros
     * @return (kg/pda)
     */
    private BigDecimal calcularConsumoUnitario(BigDecimal requerimientoTela, int pedidos){
        BigDecimal pedidosBD = BigDecimal.valueOf(pedidos);
        return requerimientoTela.divide(pedidosBD, 3, RoundingMode.HALF_UP);
    }

    /**
     * @param consumoUnitario (kg/pda)
     * @param precioTela (S./kg)
     * @return Costo unitario de materia prima
     */
    private BigDecimal calcularCostoUnitario(BigDecimal consumoUnitario, BigDecimal precioTela){
        return consumoUnitario.multiply(precioTela);
    }
    
    private BigDecimal calcularCostoInsumos(ArrayList<Double> insumos){
        BigDecimal total = BigDecimal.valueOf(0.0);
        for(Double insumo : insumos){
            total = total.add(BigDecimal.valueOf(insumo));
        }

        return total;
    }

    private BigDecimal calcularMaterialesIndirectos(ArrayList<Double> materialesIndirectos){
        BigDecimal total = BigDecimal.valueOf(0.0);
        for(Double mi : materialesIndirectos){
            total = total.add(BigDecimal.valueOf(mi));
        }

        return total;
    }

    private BigDecimal calcularGastosIndirectos(ArrayList<Double> gastosIndirectos){
        BigDecimal total = BigDecimal.valueOf(0.0);
        for(Double gi : gastosIndirectos){
            total = total.add(BigDecimal.valueOf(gi));
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
