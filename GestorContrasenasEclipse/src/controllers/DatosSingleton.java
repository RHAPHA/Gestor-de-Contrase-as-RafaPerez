package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelo.ClaseAccesoria;
import modelo.Contrasena;
import modelo.Datos;

import java.util.ArrayList;

public class DatosSingleton {

    private static DatosSingleton instancia = null;//1

    private Datos datos;//2

    private DatosSingleton(){
        this.datos = new Datos();
        datos.getListadoContra();
        //System.out.println("en contructor de Datos_Singleton: ");
        //ClaseAccesoria.imprirArray(datos.getListadoContra());
    }//3

    public static DatosSingleton getInstancia(){//4
        if(instancia == null){
            instancia = new DatosSingleton();
        }
        return instancia;
    }

    private ObservableList<String> listaCon_O_L = FXCollections.observableArrayList();

   // private String carpetaLogin;

    //private ArrayList<Contrasena> arrayContrasenas;// = new ArrayList<>();

    public ArrayList<Contrasena> getArrayContrasenas() {
        //return arrayContrasenas;
        return datos.getListadoContra();
    }

    public void setArrayContrasenas(ArrayList<Contrasena> arrayContrasenas) {
        datos.setListadoContra(arrayContrasenas);
    }


    public String getCarpetaLogin() {

        //return carpetaLogin;
        return datos.getCarpetaLogin();
    }

    public void setCarpetaLogin(String carpetaLogin) {

        //this.carpetaLogin = carpetaLogin;
        datos.setCarpetaLogin(carpetaLogin);
    }

    /*public ObservableList<String> rellenarO_L(){
        this.listaCon_O_L.clear();
        this.listaCon_O_L.addAll(datos.getListadoContra());
        return this.listaCon_O_L;
    }*/

    public int agregarContrasena(Contrasena con){
        int num = datos.agregarContrasena(con);
        return num;
    }
    public Contrasena buscarContrasenya(Contrasena c){
        Contrasena con = datos.buscarContrasena(c);
        return con;
    }

    public Contrasena buscarContraXTitulo(String titulo){
       Contrasena c = datos.buscarContrasenaXTitulo(titulo);
       return c;
    }

    public boolean eliminarContrasena(Contrasena con){
        boolean eliminado = datos.eliminarContraseña(con);
        return eliminado;
    }

    public void modificarContrasena(Contrasena c, String newTit, String newCon, String newDescrip){
        datos.modificarContrasena(c, newTit, newCon, newDescrip);
    }


    public Contrasena buscarContraXTitulo2(String titulo) {
        return null;
    }
}
