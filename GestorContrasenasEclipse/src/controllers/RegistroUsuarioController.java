package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.ClaseAccesoria;
import modelo.Contrasena;
import modelo.Encriptacion;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class RegistroUsuarioController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword_1;

    @FXML
    private PasswordField txtPassword_2;

    private String carpetaLogin;

    @FXML
    void onAc_Cancelar(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
    }

    @FXML
    void onAc_Registrarse(ActionEvent event) throws NoSuchAlgorithmException {

        //Si: no está vacio el campo del Login Y no está vacio el campo del password1 Y no está vacio el campo del password2
        if (!txtLogin.getText().isEmpty() && !txtPassword_1.getText().isEmpty() && !txtPassword_2.getText().isEmpty()) {
            boolean loginValido = ClaseAccesoria.cumpleExpresionRegular("^[A-Za-z0-9]*$", txtLogin.getText()) && txtLogin.getText().length() < 25;

            if (loginValido) {

                if (txtPassword_1.getText().equals(txtPassword_2.getText())) {

                    boolean loginDisponible = true;//si el login escrito por el usuario está disponible
                    ArrayList<String> listadoFicheros = listarFicherosDirectorio();
                    for (String s : listadoFicheros) {
                        if (s.equals(txtLogin.getText())) {//si hay ya un login asi dentro de ListadoFicheros
                            loginDisponible = false;   //en ese caso el Login no estará disponible y lo establece como false
                        }
                    }
                    if (loginDisponible) {
                        carpetaLogin = txtLogin.getText();
                        File f = new File("." + carpetaLogin);
                        try {
                           boolean fileCreado = f.createNewFile();
                            System.out.println("fileCreado->"+fileCreado);
                        } catch (IOException e) {
                            ClaseAccesoria.Dialogo("Error en createNewFile-> " + e.getMessage(), ERROR);
                            e.printStackTrace();
                        }
                        crearDirectorio(carpetaLogin);

                        Contrasena c = new Contrasena();
                        c.setPassword(Encriptacion.pruebaSha256(txtPassword_1.getText()));

                        //guardarObjeto(c);
                        File file = new File(carpetaLogin + File.separator + "contrasena.rpz");
                        escribirEnfichero(file, c);
                        abrirVentanaLoginYPassword(event);


                    } else {
                        ClaseAccesoria.Dialogo("Error: El login escrito está en uso por otro Usuario. Debe escribir uno nuevo", ERROR);
                    }

                } else {
                    ClaseAccesoria.Dialogo("Error: Las dos contraseñas no son iguales", ERROR);
                }

            } else {
                ClaseAccesoria.Dialogo("Error: El login no puede contener caracteres especiales" +
                        "Y su longitud no debe ser mayor de 25 caracteres", ERROR);
            }

        } else {
            ClaseAccesoria.Dialogo("Error: Por favor tiene que escribir los 3 campos que se piden", ERROR);
        }

    }

    private void abrirVentanaLoginYPassword(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try {
            FXMLLoader cargador = new FXMLLoader(RegistroUsuarioController.class.getResource("/vistas/LoginYPassword.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Ventana Login");
            escenario.setResizable(false);
            escenario.show();

        } catch (IOException e) {
            ClaseAccesoria.Dialogo("Error: " + e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    private ArrayList<String> listarFicherosDirectorio() {
        File file = new File(".");
        ArrayList<String> listaFicheros = null;
        if (file.exists()) {
            File[] listado = file.listFiles();

            if (listado.length > 0) {
                listaFicheros = new ArrayList<>();

                for (File i : listado) {
                    listaFicheros.add(i.getName());
                }
            }
        }
        return listaFicheros;
    }

    private boolean crearDirectorio(String nombreCarpeta) {
        File f = new File(nombreCarpeta);
        boolean directorioCreado = false;
        if (f.exists() || f.mkdir()) {

            directorioCreado = true;
        } else {
            ClaseAccesoria.Dialogo("El directorio no se ha creado", INFORMATION);
        }
        return directorioCreado;
    }


    /**
     * Método obtenido directamente de PROG9Apoyo con serializacion independiente.
     * Este metodo guarda el objeto que se le pasa por parámetro en el fichero que se le pasa por parametro
     *
     * @param objeto
     */

    public void guardarObjeto(Object objeto) {

        // FileOutputStream fos = null;
        ObjectOutputStream flujoSalida = null;
        //File fichero = new File(nombreFichero);
        File f = new File(carpetaLogin + File.separator + "contrasena.rpz");
        try {

            // f.createNewFile();
            // flujoSalida = new ObjectOutputStream(new FileOutputStream(f));
            // fos= new FileOutputStream(f);

            flujoSalida = new ObjectOutputStream(new FileOutputStream(f));

            flujoSalida.writeObject(objeto);

            flujoSalida.close();

        } catch (FileNotFoundException ex) {
            ClaseAccesoria.Dialogo("Error: " + ex.getMessage(), ERROR);
            ex.printStackTrace();

        } catch (IOException ex) {
            ClaseAccesoria.Dialogo("Error: " + ex.getMessage(), ERROR);
            ex.printStackTrace();
        }

    }

    public void escribirEnfichero(File fichero, Object objeto) {
        ObjectOutputStream fSalida = null;

        try {
            fSalida = new ObjectOutputStream(new FileOutputStream(fichero));
            //Escribe los datos en el fichero.
            fSalida.writeObject(objeto);
            ClaseAccesoria.Dialogo("Datos escritos en el fichero " + fichero.getName() + ".", INFORMATION);
        } catch (IOException e) {
            ClaseAccesoria.Dialogo(e.getMessage(), ERROR);
        } finally {
            if (fSalida != null) {
                try {
                    fSalida.close();   //Cierra el fichero.
                } catch (IOException e) {
                    ClaseAccesoria.Dialogo(e.getMessage(), ERROR);
                }
            }
        }
    }
}
