package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.ClaseAccesoria;
import modelo.Contrasena;
import modelo.Encriptacion;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Callable;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;


public class AgregarContrasenaController implements Initializable {

    @FXML
    private AnchorPane aP_FormatoCon;

    @FXML
    private CheckBox cBoxFormato;

    @FXML
    private CheckBox cBoxVisualizar;

    @FXML
    private Label lbCaracteresRestantes;

    @FXML
    private PasswordField txtContrasena_Oculta;

    @FXML
    private TextField txtContrasena_Visible;

    @FXML
    private TextArea txtDescrip;

    @FXML
    private TextField txtEspeciales;

    @FXML
    private TextField txtMayusculas;

    @FXML
    private TextField txtMinusculas;

    @FXML
    private TextField txtNumeros;

    @FXML
    private TextField txtTitulo;

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

        aP_FormatoCon.setDisable(true);

        datosSingleton = DatosSingleton.getInstancia();
        System.out.println("datosSingleton.getCarpetaLogin()-> " + datosSingleton.getCarpetaLogin());
    }

    @FXML
    void onAc_Cancelar(ActionEvent event) {

        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try {
            FXMLLoader cargador = new FXMLLoader(AgregarContrasenaController.class.getResource("/vistas/MenuPrincipal.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Menú Principal");
            escenario.setResizable(false);
            escenario.show();

        } catch (IOException e) {
            ClaseAccesoria.Dialogo("Error: " + e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void onAc_GenerarContra(ActionEvent event) {

        if (txtMinusculas.getText().isEmpty()) txtMinusculas.setText("0");
        if (txtMayusculas.getText().isEmpty()) txtMayusculas.setText("0");
        if (txtEspeciales.getText().isEmpty()) txtEspeciales.setText("0");
        if (txtNumeros.getText().isEmpty()) txtNumeros.setText("0");

        String c = generaContrasena();
        txtContrasena_Oculta.setText(c);
        txtContrasena_Visible.setText(c);
    }

    @FXML
    void onAc_VisualiceContra(ActionEvent event) {

        if (cBoxVisualizar.isSelected()) {
            txtContrasena_Visible.setVisible(true);
            txtContrasena_Oculta.setVisible(false);
            txtContrasena_Visible.setText(txtContrasena_Oculta.getText());
        } else {
            txtContrasena_Visible.setVisible(false);
            txtContrasena_Oculta.setVisible(true);
            txtContrasena_Oculta.setText(txtContrasena_Visible.getText());
        }
    }

    @FXML
    void onAct_Aceptar(ActionEvent event) {

        //si el campo visible está escrito contrasena = campoVisible caso contrario contrasena = campoOculto
        String contrasena = !txtContrasena_Visible.getText().isEmpty() ? txtContrasena_Visible.getText() : txtContrasena_Oculta.getText();

        if (!txtTitulo.getText().isEmpty() && contrasena != null) {

            if (txtTitulo.getText().length() <= 45 && txtTitulo.getText().length() >= 5) {

                System.out.println("contrasena " + contrasena);

                if (contrasena.length() <= 45 && contrasena.length() >= 5) {
                    Contrasena con = new Contrasena();
                    con.setTitulo(txtTitulo.getText());

                    try {
                        con.setPassword(Encriptacion.Encriptar(contrasena));
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    }
                    con.setDescripción(txtDescrip.getText());

                    datosSingleton = DatosSingleton.getInstancia();
                    int num = datosSingleton.agregarContrasena(con);

                    switch (num) {
                        case -1://ya hay una contraseña con ese titulo en el Array y no se añadirá
                            ClaseAccesoria.Dialogo("Ya hay una contraseña con ese titulo en el Array y no se añadirá", ERROR);
                            break;

                        case 0:
                            ClaseAccesoria.Dialogo("La contraseña se ha añadido correctamente", ERROR);

                            //if (Objects.nonNull(datosSingleton.getArrayContrasenas()))
                                ClaseAccesoria.imprirArray(datosSingleton.getArrayContrasenas());
                            break;

                        case 1:
                            ClaseAccesoria.Dialogo("Hay mas de 50 contraseñas en el array y no se van a añadir más", ERROR);
                            break;
                    }

                    abrirVentanaMenuPrincipal(event);

                } else {
                    ClaseAccesoria.Dialogo("La contraseña debe tener una extensión entre 5 y 45 caracteres", ERROR);
                }
            } else {
                ClaseAccesoria.Dialogo("La longitud del Título debe ser entre 5 y 45 caracteres", ERROR);
            }
        } else {
            ClaseAccesoria.Dialogo("Debe escribir al menos los campos Título y Contraseña", ERROR);
        }
    }

    private void abrirVentanaMenuPrincipal(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try {
            FXMLLoader cargador = new FXMLLoader(AgregarContrasenaController.class.getResource("/vistas/MenuPrincipal.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Menu Principal");
            escenario.setResizable(false);
            escenario.show();

        } catch (IOException e) {
            ClaseAccesoria.Dialogo("Error: " + e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    void onAct_FormatoContra(ActionEvent event) {
        if (cBoxFormato.isSelected()) {
            aP_FormatoCon.setDisable(false);
        } else {
            aP_FormatoCon.setDisable(true);
        }
    }

    @FXML
    void onKey_caracteresREstantes(KeyEvent event) {

        String descripcion = txtDescrip.getText();
        int longitud = descripcion.length();

        int caracteresRestantes = 199 - longitud;
        lbCaracteresRestantes.setText("" + caracteresRestantes);
        if (caracteresRestantes <= 0) {
            ClaseAccesoria.Dialogo("Se ha agotado la extensión permitida para hacer la Descripcion de la contraseña", INFORMATION);
        }

    }

    private String generaContrasena() {
        String mayus, minus, especiales, num, resultado = "";

        mayus = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        minus = "abcdefghijklmnñopqrstuvwxyz";
        num = "0123456789";
        especiales = "!@#$%&/()=?¿¡![]{};:_,.-*+<>ç";

        try {
            int numMinus = Integer.parseInt(txtMinusculas.getText());
            resultado = aleatorio(resultado, minus, numMinus);

            int numMayus = Integer.parseInt(txtMayusculas.getText());
            resultado = aleatorio(resultado, mayus, numMayus);

            int numEspe = Integer.parseInt(txtEspeciales.getText());
            resultado = aleatorio(resultado, especiales, numEspe);

            int numNumeros = Integer.parseInt(txtNumeros.getText());
            resultado = aleatorio(resultado, num, numNumeros);

        } catch (NumberFormatException e) {
            ClaseAccesoria.Dialogo("Error al escribir el número de caracteres" + e.getMessage(), ERROR);
            //e.printStackTrace();
        }
        System.out.println(resultado);
        String arrayPalabras[] = resultado.split("");
        Collections.shuffle(Arrays.asList(arrayPalabras));
        resultado = "";
        for (int i = 0; i < arrayPalabras.length; i++) {
            resultado = resultado + arrayPalabras[i];
        }
        System.out.println(resultado);
        return resultado;
    }

    private String aleatorio(String resultado, String palabra, int numero) {

        String[] arrayPalabra = palabra.split("");
        int numeroAleatorio = 0;

        for (int i = 0; i < numero; i++) {
            numeroAleatorio = (int) (Math.random() * (arrayPalabra.length - 1) + 0);

            resultado = resultado + arrayPalabra[numeroAleatorio];
        }
        return resultado;
    }

    @FXML
    void onKey_soloAdmitenNumeros(KeyEvent event) {

       /* Callable<Object> callable = new Callable<Object>() {
            public Object call() throws Exception {
            }
        };*/
        boolean esNumero = ClaseAccesoria.cumpleExpresionRegular("^[0-9]*$", txtMinusculas.getText());
        if (!esNumero) {
            ClaseAccesoria.Dialogo("Este campo recoge el NÚMERO de caracteres en minúsculas no admite otro tipo de caracteres", ERROR);
        }
    }


}
