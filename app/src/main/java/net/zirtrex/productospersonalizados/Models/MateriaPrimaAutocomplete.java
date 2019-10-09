package net.zirtrex.productospersonalizados.Models;

public class MateriaPrimaAutocomplete {

    private String nombreMateriaPrima;
    private String descripcionMateriaPrima;

    public MateriaPrimaAutocomplete() {}

    public MateriaPrimaAutocomplete(String nombreMateriaPrima, String descripcionMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
        this.descripcionMateriaPrima = descripcionMateriaPrima;
    }

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public String getDescripcionMateriaPrima() {
        return descripcionMateriaPrima;
    }

    public void setDescripcionMateriaPrima(String descripcionMateriaPrima) {
        this.descripcionMateriaPrima = descripcionMateriaPrima;
    }

    @Override
    public String toString() {
        return getNombreMateriaPrima();
    }
}
