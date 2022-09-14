package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Datos implements Serializable {

    private String carpetaLogin;
    private ArrayList<Contrasena> listadoContra ;

    public Datos(/*String carpetaLogin,ArrayList<Contrasena> listadoContra*/) {
        //this.carpetaLogin = carpetaLogin;
      // Contrasena c = new Contrasena("Contraseña Ejemplo", "contraseña", "Esta es una contraseña de pruebas");
        this.listadoContra = new ArrayList<>();
        //listadoContra.add(c);
       // System.out.println("en contructor de Datos");
        ClaseAccesoria.imprirArray(listadoContra);
    }

   // public Datos() {
    //}

    public String getCarpetaLogin() {
        return carpetaLogin;
    }

    public void setCarpetaLogin(String carpetaLogin) {
        this.carpetaLogin = carpetaLogin;
    }

    public ArrayList<Contrasena> getListadoContra() {
        return listadoContra;
    }

    public void setListadoContra(ArrayList<Contrasena> listadoContra) {
        this.listadoContra = listadoContra;
    }

    public int agregarContrasena(Contrasena con){

        int num = -10;
       if(listadoContra == null){
            //listadoContra = new ArrayList<>();
           listadoContra.add(con);
            System.out.println("En agregarContraseña en Datos el ArrayList es nulo");
            num=0;
        }else{
            if(listadoContra.size() < 50){
                Contrasena c = buscarContrasena(con);
                if(c==null){//c==null es que el titulo de la contraseña del parametro no está en al ArrayList
                    listadoContra.add(con);
                    num=0;
                }else{
                    //ya hay una contraseña con ese titulo en el Array y no se añadirá
                    num = -1;
                }
            }else{
//hay mas de 50 contraseñas en el array y no se van a añadir más
                num = 1;
            }
        }
        return num;
    }

    /**
     * Devuelve null si el titulo de la contraseña que se da por parametro no se encuentra en el ArrayList
     * caso contrario devuelve el objeto contraseña cuyo titulo coincide con el titulo de la contraseña que
     * se da por parametro
     * @param con
     * @return
     */
    public Contrasena buscarContrasena(Contrasena con){

        Contrasena c=null;
        if(listadoContra == null ){
            c = null;
        }else{
            for(int i=0; i<listadoContra.size(); i++){
                //si el titulo de la contrasena del argumento coincide con alguna de los titulos de las contraseñas
                //del array devuelve dicha contraseña
                if(listadoContra.get(i).getTitulo().equals(con.getTitulo())){
                    c = con;
                }//si el titulo de la contrasena del argumento es igual entonces devuelve null
            }
        }
        return c;
    }

    /**
     * Este metodo recibe un String con el que compara con el titulo de los objetos Contrasena del Array
     * si dicho String es igual al titulo de un objeto contrasena entonces lo retorna, caso contrario retorna null.
     * @param titulo
     * @return
     */
    public Contrasena buscarContrasenaXTitulo(String titulo){

        Contrasena con = null;
        for(int i=0; i<listadoContra.size(); i++){
            if(titulo.equals(listadoContra.get(i).getTitulo())){
                con = listadoContra.get(i);
            }
        }
        return con;
    }

    public Contrasena buscarContrasenaXTitulo2(String titulo){

        Contrasena con = null;
        for(int i=0; i<listadoContra.size(); i++){
            if(titulo.equals(listadoContra.get(i).getTitulo())){
                con = listadoContra.get(i);
            }
        }
        return con;
    }

    public boolean eliminarContraseña(Contrasena contrasena){
        int tamanioArray = listadoContra.size();
        listadoContra.remove(contrasena);
        int newTamanioArray = listadoContra.size();
        if (tamanioArray -1 == newTamanioArray){
            return true;
        }
        else return false;
    }

    public Contrasena modificarContrasena(Contrasena c, String newTitulo, String newPassword, String newDescripcion){

        c.setTitulo(newTitulo);
        c.setPassword(newPassword);
        c.setDescripción(newDescripcion);

        return c;
    }
}
