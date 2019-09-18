package net.zirtrex.productospersonalizados.Models;

public class Usuarios {

    private int idUsuario;
    private String email;
    private String rol;

    public Usuarios() {}

    public Usuarios(String email, String rol) {
        this.setEmail(email);
        this.setRol(rol);
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuarios{" +
                "idUsuario=" + idUsuario +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
