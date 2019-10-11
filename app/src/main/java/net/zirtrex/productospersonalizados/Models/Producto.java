package net.zirtrex.productospersonalizados.Models;

import java.util.Map;

public class Producto {

    private String idProducto;
    private String idUsuario;
    private String nombreProducto;
    private Double precio;
    private String imgUrl;
    private String tipo;
    private Double gastosFinancieros;
    private Map <String, Double> materiaPrima;
    private Map <String, Double> materialesIndirectos;
    private Map <String, Double> materialesIndirectosFabricacion;

    public Producto() {}

    public Producto(String nombreProducto, Map <String, Double> materiaPrima) {
        this.nombreProducto = nombreProducto;
        this.materiaPrima = materiaPrima;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreProducto() {
        return this.nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getGastosFinancieros() {
        return gastosFinancieros;
    }

    public void setGastosFinancieros(Double gastosFinancieros) {
        this.gastosFinancieros = gastosFinancieros;
    }

    public Map <String, Double> getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(Map <String, Double> materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    public Map<String, Double> getMaterialesIndirectos() {
        return materialesIndirectos;
    }

    public void setMaterialesIndirectos(Map<String, Double> materialesIndirectos) {
        this.materialesIndirectos = materialesIndirectos;
    }

    public Map<String, Double> getMaterialesIndirectosFabricacion() {
        return materialesIndirectosFabricacion;
    }

    public void setMaterialesIndirectosFabricacion(Map<String, Double> materialesIndirectosFabricacion) {
        this.materialesIndirectosFabricacion = materialesIndirectosFabricacion;
    }

}
