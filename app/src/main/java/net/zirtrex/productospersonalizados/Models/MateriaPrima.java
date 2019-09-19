package net.zirtrex.productospersonalizados.Models;

public class MateriaPrima {

    private int mermaDeCorte;
    private int cantidadDePrenda;
    private Double anchoTela;
    private Double largoTela;
    private Double orillo;
    private Double punto;
    private int pedido;
    private int mermaDeSegunda;
    private Double dendidadDeTela;

    public MateriaPrima() {}

    public MateriaPrima(int mermaDeCorte, int cantidadDePrenda, Double anchoTela, Double largoTela, Double orillo, Double punto, int pedido, int mermaDeSegunda, Double dendidadDeTela) {
        this.mermaDeCorte = mermaDeCorte;
        this.cantidadDePrenda = cantidadDePrenda;
        this.anchoTela = anchoTela;
        this.largoTela = largoTela;
        this.orillo = orillo;
        this.punto = punto;
        this.pedido = pedido;
        this.mermaDeSegunda = mermaDeSegunda;
        this.dendidadDeTela = dendidadDeTela;
    }

    public int getMermaDeCorte() {
        return mermaDeCorte;
    }

    public void setMermaDeCorte(int mermaDeCorte) {
        this.mermaDeCorte = mermaDeCorte;
    }

    public int getCantidadDePrenda() {
        return cantidadDePrenda;
    }

    public void setCantidadDePrenda(int cantidadDePrenda) {
        this.cantidadDePrenda = cantidadDePrenda;
    }

    public Double getAnchoTela() {
        return anchoTela;
    }

    public void setAnchoTela(Double anchoTela) {
        this.anchoTela = anchoTela;
    }

    public Double getLargoTela() {
        return largoTela;
    }

    public void setLargoTela(Double largoTela) {
        this.largoTela = largoTela;
    }

    public Double getOrillo() {
        return orillo;
    }

    public void setOrillo(Double orillo) {
        this.orillo = orillo;
    }

    public Double getPunto() {
        return punto;
    }

    public void setPunto(Double punto) {
        this.punto = punto;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    public int getMermaDeSegunda() {
        return mermaDeSegunda;
    }

    public void setMermaDeSegunda(int mermaDeSegunda) {
        this.mermaDeSegunda = mermaDeSegunda;
    }

    public Double getDendidadDeTela() {
        return dendidadDeTela;
    }

    public void setDendidadDeTela(Double dendidadDeTela) {
        this.dendidadDeTela = dendidadDeTela;
    }

    @Override
    public String toString() {
        return "MateriaPrima{" +
                "mermaDeCorte=" + mermaDeCorte +
                ", cantidadDePrenda=" + cantidadDePrenda +
                ", anchoTela=" + anchoTela +
                ", largoTela=" + largoTela +
                ", orillo=" + orillo +
                ", punto=" + punto +
                ", pedido=" + pedido +
                ", mermaDeSegunda=" + mermaDeSegunda +
                ", dendidadDeTela=" + dendidadDeTela +
                '}';
    }
}
