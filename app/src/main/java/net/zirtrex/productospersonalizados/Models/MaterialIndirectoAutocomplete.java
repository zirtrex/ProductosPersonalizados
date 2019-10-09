package net.zirtrex.productospersonalizados.Models;

public class MaterialIndirectoAutocomplete {

    private String nombreMaterialIndirecto;
    private String descripcionMaterialIndirecto;

    public MaterialIndirectoAutocomplete() {}

    public MaterialIndirectoAutocomplete(String nombreMaterialIndirecto, String descripcionMaterialIndirecto) {
        this.nombreMaterialIndirecto = nombreMaterialIndirecto;
        this.descripcionMaterialIndirecto = descripcionMaterialIndirecto;
    }

    public String getNombreMaterialIndirecto() {
        return nombreMaterialIndirecto;
    }

    public void setNombreMaterialIndirecto(String nombreMaterialIndirecto) {
        this.nombreMaterialIndirecto = nombreMaterialIndirecto;
    }

    public String getDescripcionMaterialIndirecto() {
        return descripcionMaterialIndirecto;
    }

    public void setDescripcionMaterialIndirecto(String descripcionMaterialIndirecto) {
        this.descripcionMaterialIndirecto = descripcionMaterialIndirecto;
    }

    @Override
    public String toString() {
        return getNombreMaterialIndirecto();
    }
}
