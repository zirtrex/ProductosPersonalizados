package net.zirtrex.productospersonalizados.Models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class InversionesContent {

    public static final List<Inversiones> INVERSIONES = new ArrayList<Inversiones>();

    private static final int COUNT = 36;

    private int[] nroCuotas = {3, 6, 9, 12, 18, 24, 36};
    private double[] porcentajeInteres = {2.68, 4.73, 6.81, 8.91, 13.19, 17.58, 26.67};

    double montoTotal = 0;
    double impuesto;

    public InversionesContent(double montoTotal) {
        this.montoTotal = montoTotal;
        this.impuesto = this.montoTotal * 0.12;
        INVERSIONES.removeAll(INVERSIONES);

        for (int i = 1; i < nroCuotas.length; i++) {

            double montoBruto = this.montoTotal + this.impuesto + porcentajeInteres[i];
            double montoCuota = montoBruto / nroCuotas[i];

            String convertMontoCuota = NumberFormat.getCurrencyInstance().format(montoCuota);

            addInversion(crearInversion(nroCuotas[i], convertMontoCuota));
        }
    }

    private static void addInversion(Inversiones item) {
        INVERSIONES.add(item);
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
