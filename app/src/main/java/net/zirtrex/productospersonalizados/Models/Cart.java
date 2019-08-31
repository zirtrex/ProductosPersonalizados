package net.zirtrex.productospersonalizados.Models;


public class Cart {

    private String cartId;
    private String cartNombreProducto;
    private Double cartPrecio;
    private String cartImgUrl;
    private int cartCantidad;
    private Double cartPrecioTotal;

    public Cart() {}

    public Cart(String cartNombreProducto) {
        this.cartNombreProducto = cartNombreProducto;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String carId) {
        this.cartId = carId;
    }

    public String getCartNombreProducto() {
        return cartNombreProducto;
    }

    public void setCartNombreProducto(String cartNombreProducto) {
        this.cartNombreProducto = cartNombreProducto;
    }

    public Double getCartPrecio() {
        return cartPrecio;
    }

    public void setCartPrecio(Double cartPrecio) {
        this.cartPrecio = cartPrecio;
    }

    public String getCartImgUrl() {
        return cartImgUrl;
    }

    public void setCartImgUrl(String cartImgUrl) {
        this.cartImgUrl = cartImgUrl;
    }

    public int getCartCantidad() {
        return cartCantidad;
    }

    public void setCartCantidad(int cartCantidad) {
        this.cartCantidad = cartCantidad;
    }

    public Double getCartPrecioTotal() {
        return cartPrecioTotal;
    }

    public void setCartPrecioTotal(Double cartPrecioTotal) {
        this.cartPrecioTotal = cartPrecioTotal;
    }

}
