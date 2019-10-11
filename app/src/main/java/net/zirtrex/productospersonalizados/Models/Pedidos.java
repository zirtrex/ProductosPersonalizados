package net.zirtrex.productospersonalizados.Models;


public class Pedidos {

    private String idPedido;
    private String idCliente; //Usuario que hace el pedido
    private String idProveedor; //Fabricante que va el pedido
    private String idProducto; //Producto seleccionado
    private String nombreProducto;
    private Double precio;
    private String imgUrl;
    private Double anchoPrenda;
    private Double largoPrenda;
    private int cantidad;
    private Double total;

    public Pedidos() {}

    public Pedidos(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
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

    public int getCantidad() {
        return cantidad;
    }

    public Double getAnchoPrenda() {
        return anchoPrenda;
    }

    public void setAnchoPrenda(Double anchoPrenda) {
        this.anchoPrenda = anchoPrenda;
    }

    public Double getLargoPrenda() {
        return largoPrenda;
    }

    public void setLargoPrenda(Double largoPrenda) {
        this.largoPrenda = largoPrenda;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }


}
