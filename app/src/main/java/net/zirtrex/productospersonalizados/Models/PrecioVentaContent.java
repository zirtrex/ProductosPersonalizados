package net.zirtrex.productospersonalizados.Models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class PrecioVentaContent {

    Productos producto;

    private String[] materiaPrima = {"orillo","pedidos", "punto",
            "anchoTela", "cantidadDePrenda", "densidadTela",
            "largoTela", "mermaDeCorte", "mermaDeSegunda"};

    public PrecioVentaContent(Productos producto) {
        this.producto = producto;



        for (int i = 1; i < nroCuotas.length; i++) {

            double montoBruto = this.montoTotal + this.impuesto + porcentajeInteres[i];
            double montoCuota = montoBruto / nroCuotas[i];

            String convertMontoCuota = NumberFormat.getCurrencyInstance().format(montoCuota);

            addInversion(crearInversion(nroCuotas[i], convertMontoCuota));
        }
    }

    private void calcularConsumoLineal(Double largoTela, ) {

    }

    private static Inversiones crearInversion(int nroCuota, String montoCuota) {
        return new Inversiones(String.valueOf(nroCuota), "Inversiones de ", montoCuota);
    }

    /**
     * Constructor para el listado de las inversiones
     */
    public static class Inversiones {
        public final String nroCuotas;
        public final String texto;
        public final String monto;

        public Inversiones(String nroCuotas, String texto, String monto) {
            this.nroCuotas = nroCuotas;
            this.texto = texto;
            this.monto = monto;
        }

        @Override
        public String toString() {
            return nroCuotas + texto + monto;
        }
    }
}
