package net.zirtrex.productospersonalizados.Models;

import java.util.Map;

public class Productos{

    private String idProducto;
    private String idUsuario;
    private String nombreProducto;
    private Double precio;
    private String imgUrl;
    private Map <String, Double> materiaPrima;


    public Productos() {}

    public Productos(String nombreProducto, Map <String, Double> materiaPrima) {
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

    public Map <String, Double> getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(Map <String, Double> materiaPrima) {
        this.materiaPrima = materiaPrima;
    }
}
