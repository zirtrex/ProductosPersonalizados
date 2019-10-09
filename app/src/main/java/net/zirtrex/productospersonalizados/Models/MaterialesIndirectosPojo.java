package net.zirtrex.productospersonalizados.Models;

public class MaterialesIndirectosPojo {

    private String nombreMaterialIndirecto;
    private Double valorMaterialIndirecto;

    public MaterialesIndirectosPojo() {}

    public MaterialesIndirectosPojo(String nombreMaterialIndirecto, Double valorMaterialIndirecto) {
        this.nombreMaterialIndirecto = nombreMaterialIndirecto;
        this.valorMaterialIndirecto = valorMaterialIndirecto;
    }

    public String getNombreMaterialIndirecto() {
        return nombreMaterialIndirecto;
    }

    public void setNombreMaterialIndirecto(String nombreMaterialIndirecto) {
        this.nombreMaterialIndirecto = nombreMaterialIndirecto;
    }

    public Double getValorMaterialIndirecto() {
        return valorMaterialIndirecto;
    }

    public void setValorMaterialIndirecto(Double valorMaterialIndirecto) {
        this.valorMaterialIndirecto = valorMaterialIndirecto;
    }
}
