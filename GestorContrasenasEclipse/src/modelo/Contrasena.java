package modelo;

import java.io.Serializable;

public class Contrasena implements Serializable {

    private String titulo;
    private String password;
    private String descripción;

    public Contrasena(String titulo, String password, String descripción) {
        this.titulo = titulo;
        this.password = password;
        this.descripción = descripción;
    }

    public Contrasena(String password) {
        this.password = password;
    }

    public Contrasena(String titulo, String password) {
        this.titulo = titulo;
        this.password = password;
    }

    public Contrasena() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    @Override
    public String toString() {
        return "Contrasena{" +
                "titulo='" + titulo + '\'' +
                ", password='" + password + '\'' +
                ", descripción='" + descripción + '\'' +
                '}';
    }
}
