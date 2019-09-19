package net.zirtrex.productospersonalizados.Models;

public class MateriaPrimaPojo {

    private String nombreMateriaPrima;
    private double valorMateriaPrima;

    public MateriaPrimaPojo() {}

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public double getValorMateriaPrima() {
        return valorMateriaPrima;
    }

    public void setValorMateriaPrima(double valorMateriaPrima) {
        this.valorMateriaPrima = valorMateriaPrima;
    }

    @Override
    public String toString() {
        return "MateriaPrimaPojo{" +
                "nombreMateriaPrima='" + nombreMateriaPrima + '\'' +
                ", valorMateriaPrima=" + valorMateriaPrima +
                '}';
    }
}
