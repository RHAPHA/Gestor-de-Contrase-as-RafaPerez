package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.ClaseAccesoria;
import modelo.Contrasena;
import modelo.Encriptacion;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class LoginYPassWordControler implements Initializable {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    private String carpetaLogin;

    private DatosSingleton datosSingleton;

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datosSingleton = DatosSingleton.getInstancia();
        System.out.println("datosSingleton"+datosSingleton.getCarpetaLogin());
    }

    @FXML
    void onAc_IniciarSesion(ActionEvent event) {

        if(!txtLogin.getText().isEmpty() && !txtPassword.getText().isEmpty()){

            carpetaLogin = txtLogin.getText();
            File f = new File("."+File.separator+carpetaLogin);
            if(f.exists() && f.isDirectory()){
                //ahora llevamos a cabo la des-serializacion y la comparación de la contraseña escrita y la des-serializada
                File fi = new File("."+File.separator+carpetaLogin+File.separator+"contrasena.rpz");

                Contrasena c = (Contrasena) leerDeFichero(fi);
                //System.out.println(c.getPassword()+"  "+Encriptacion.pruebaSha256(txtPassword.getText()));

                if(c.getPassword().equals(Encriptacion.pruebaSha256(txtPassword.getText()))){

                    //si toddo es correcto entonces deserializamos el arrayList y asignamos carpetaLogin y el arrayList Des serializado a los atributos de la instancia Singleton
                    //creamos File que apunta a contrasenia2.rpz
                    File file = new File("."+File.separator+carpetaLogin+File.separator+"contrasenia2.rpz");
                    ArrayList<Contrasena> arrayL = null;

                    if(file.exists() && file.isFile()){//si ya se ha creado, si hemos usado otras veces la aplicacion
                        arrayL = (ArrayList<Contrasena>) leerDeFichero(file);//llevamos a cabo la des-serializacion y guardamos el arrayList serializado en una variable


                        datosSingleton.setArrayContrasenas(arrayL);//asignamos el arraylist des-serializado a datosSingleton (es la instancia unica)
                        datosSingleton.setCarpetaLogin(carpetaLogin);//IMPORTANTE: EN ESTA LINEA ASIGNAMOS EL LOGIN A LA INSTANCIA SINGLETON

                        if (Objects.nonNull(arrayL))
                            ClaseAccesoria.imprirArray(arrayL);


                        abrirVentanaMenuPrincipal(event);

                    }else{//si es la primera vez que usamos la app
                        ClaseAccesoria.Dialogo("No existe contrasenia2.rpz", INFORMATION);

                       // datosSingleton.setArrayContrasenas(arrayL);//asignamos el arraylist des-serializado a datosSingleton (es la instancia unica)
                        //arrayL = new ArrayList<>();
                        //datosSingleton.setArrayContrasenas(arrayL);
                        datosSingleton.setCarpetaLogin(carpetaLogin);//IMPORTANTE: EN ESTA LINEA ASIGNAMOS EL LOGIN A LA INSTANCIA SINGLETON


                        abrirVentanaMenuPrincipal(event);
                        //Mejor no lo creamos porque ahora mismo no sirve para nada, no se crea contrasenia2
                       /* try {
                            boolean contrasenia2Creado = file.createNewFile();//creamos el File que apunta a contrasenia2
                            System.out.println("contrasenia2Creado "+contrasenia2Creado);
                        } catch (IOException e) {
                            ClaseAccesoria.Dialogo("ERROR->createNewFile"+e.getMessage(), ERROR);
                            e.printStackTrace();
                        }*/
                    }


                }else{
                    ClaseAccesoria.Dialogo("Error: La contraseña escrita no es correcta", ERROR);
                }

            }else{
                ClaseAccesoria.Dialogo("Error: No hay ningún login como el que se ha escrito", ERROR);
            }

        }else{
            ClaseAccesoria.Dialogo("Error: Por favor tiene que escribir los 2 campos que se piden", ERROR);
        }

    }

    @FXML
    void onAc_Registrarse(ActionEvent event) {

        abrirVentanaRegistrarse(event);
    }

    private void abrirVentanaRegistrarse(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try{
            FXMLLoader cargador = new FXMLLoader(LoginYPassWordControler.class.getResource("/vistas/RegistroUsuario.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Registro de Usuario");
            escenario.setResizable(false);
            escenario.show();

        }catch (IOException e){
            ClaseAccesoria.Dialogo("Error: "+e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    private void abrirVentanaMenuPrincipal(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try{
            FXMLLoader cargador = new FXMLLoader(LoginYPassWordControler.class.getResource("/vistas/MenuPrincipal.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Menu Principal");
            escenario.setResizable(false);
            escenario.show();

        }catch (IOException e){
            ClaseAccesoria.Dialogo("Error: "+e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Método para leer un objeto del archivo binario.
     * @param fichero Representación de un fichero.
     * @return Retorna el objeto leido del archivo binario.
     */
    //Elimina el warning que se produce con el casting al tipo genérico.
    @SuppressWarnings("unchecked")
    public Object leerDeFichero(File fichero){
        ObjectInputStream fEntrada=null;
        Object objeto=null;

        try{
            fEntrada=new ObjectInputStream(new FileInputStream(fichero));
            //Lee los datos del fichero.
            objeto = fEntrada.readObject();
            ClaseAccesoria.Dialogo("Fichero de datos "+fichero.getName()+" cargado.",INFORMATION);
        }catch(FileNotFoundException e){
            ClaseAccesoria.Dialogo("El fichero "+fichero.getName()+" no existe.",ERROR);
        }catch(ClassNotFoundException | IOException e){
            ClaseAccesoria.Dialogo(e.getMessage(),ERROR);
        }finally{
            if(fEntrada!=null){
                try{
                    fEntrada.close();   //Cierra el fichero.
                }catch(IOException e){
                    ClaseAccesoria.Dialogo(e.getMessage(),ERROR);
                }
            }
        }
        return objeto;
    }


}

