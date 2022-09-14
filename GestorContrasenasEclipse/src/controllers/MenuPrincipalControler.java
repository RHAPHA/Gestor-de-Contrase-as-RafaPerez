package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelo.ClaseAccesoria;
import modelo.Contrasena;
import modelo.Encriptacion;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class MenuPrincipalControler implements Initializable {


    @FXML
    private CheckBox cBoxVisualice;

    @FXML
    private Label lbCaracteresRestantes;
    @FXML
    private ListView<String> listViewLista;

    @FXML
    private TextField txtContrasena_Visible;

    @FXML
    private TextArea txtDescrip;

    @FXML
    private TextField txtTitulo;

    @FXML
    private PasswordField txtcontrasena_Oculto;

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
        //ClaseAccesoria.imprirArray(datosSingleton.getArrayContrasenas());
        rellenarListView();
    }

    @FXML
    void onAc_Actualizar(ActionEvent event) {
        rellenarListView();
    }

    @FXML
    void onAc_Eliminar(ActionEvent event) {
        String titulo = listViewLista.getSelectionModel().getSelectedItem();


        if(titulo != null){
            Contrasena c = datosSingleton.buscarContraXTitulo(titulo);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("Si pulsa OK se borrará la contraseña "+titulo);
            Optional<ButtonType> action = alert.showAndWait();
            if(action.get() == ButtonType.OK){
                boolean eliminado = datosSingleton.eliminarContrasena(c);

                if(eliminado){
                    ClaseAccesoria.Dialogo("Información: La contraseña se ha eliminado de manera satisfactoria", INFORMATION);
                    txtContrasena_Visible.setText("");
                    txtcontrasena_Oculto.setText("");
                    txtTitulo.setText("");
                    txtDescrip.setText("");
                }else{
                    ClaseAccesoria.Dialogo("Información: La eliminación de la contraseña no ha tenido lugar", INFORMATION);
                }
            }else{
                event.consume();
            }
        }else{
            ClaseAccesoria.Dialogo("Error: Debe seleccionar una contraseña del listado", ERROR);
        }
    }

    @FXML
    void onAc_Salir(ActionEvent event) {

        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();

    }

    @FXML
    void onAct_Copiar(ActionEvent event) {

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();

        content.putString(txtcontrasena_Oculto.getText());

        clipboard.setContent(content);

        /* final Clipboard clipboard = Clipboard.getSystemClipboard();
     final ClipboardContent content = new ClipboardContent();
     content.putString("Some text");
     content.putHtml("<b>Some</b> text");
     clipboard.setContent(content);*/
    }

    @FXML
    void onActi_VisualizarCon(ActionEvent event) {

        if(cBoxVisualice.isSelected()){
            txtContrasena_Visible.setVisible(true);
            txtcontrasena_Oculto.setVisible(false);
            txtContrasena_Visible.setText(txtcontrasena_Oculto.getText());
        }else{
            txtContrasena_Visible.setVisible(false);
            txtcontrasena_Oculto.setVisible(true);
            txtcontrasena_Oculto.setText(txtContrasena_Visible.getText());
        }
    }

    @FXML
    void onKey_caracteresRestantes(KeyEvent event) {

        String descripcion = txtDescrip.getText();
        int longitud = descripcion.length();

        int caracteresRestantes = 199-longitud;
        lbCaracteresRestantes.setText(""+caracteresRestantes);
        if(caracteresRestantes <= 0){
            ClaseAccesoria.Dialogo("Se ha agotado la extensión permitida para hacer la Descripcion de la contraseña", INFORMATION);
        }
    }

    @FXML
    void onMouse_establecerNumCaractRestantes(MouseEvent event) {
        String descripcion = txtDescrip.getText();
        int longitud = descripcion.length();

        int caracteresRestantes = 199-longitud;
        lbCaracteresRestantes.setText(""+caracteresRestantes);
    }

    @FXML
    void on_Ac_Agregar(ActionEvent event) {
        abrirVentanaAgregar(event);
    }

    @FXML
    void on_Ac_Modificar(ActionEvent event) {

        String titulo = listViewLista.getSelectionModel().getSelectedItem();

        if(titulo != null){

            Contrasena c = datosSingleton.buscarContraXTitulo(titulo);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Confirmación");
            alert.setContentText("Si pulsa OK se modificará la contraseña "+titulo);
            Optional<ButtonType> action = alert.showAndWait();
            if(action.get() == ButtonType.OK){
                String newTitulo=txtTitulo.getText();
                String newDescrip = txtDescrip.getText();
                String newContrasena;
                try {
                    newContrasena  = Encriptacion.Encriptar(txtcontrasena_Oculto.getText());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                }
                datosSingleton.modificarContrasena(c, newTitulo, newContrasena, newDescrip);
            }else{
                event.consume();
            }
        }else{
            ClaseAccesoria.Dialogo("Error: Debe seleccionar una contraseña del listado", ERROR);
        }
    }

    @FXML
    void on_Mous_seleccionarCon(MouseEvent event) {
        String titulo =listViewLista.getSelectionModel().getSelectedItem();

        if(titulo  != null){
            Contrasena c = datosSingleton.buscarContraXTitulo(titulo);

            if(Objects.nonNull(c)){
                txtTitulo.setText(c.getTitulo());
                txtcontrasena_Oculto.setText(Encriptacion.Des_Encriptar(c.getPassword()));
                txtContrasena_Visible.setText(Encriptacion.Des_Encriptar(c.getPassword()));
                txtDescrip.setText(c.getDescripción());
            }
        }else{
           ClaseAccesoria.Dialogo("Seleccione una contraseña del listado", INFORMATION);
        }
    }

    private void abrirVentanaAgregar(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage escenario = (Stage) node.getScene().getWindow();
        escenario.close();
        try{
            FXMLLoader cargador = new FXMLLoader(MenuPrincipalControler.class.getResource("/vistas/AgregarContrasena.fxml"));
            Scene escena = new Scene(cargador.load());
            escenario.setScene(escena);
            escenario.setTitle("Agregar Contraseña ");
            escenario.setResizable(false);
            escenario.show();

        }catch (IOException e){
            ClaseAccesoria.Dialogo("Error: "+e.getMessage(), ERROR);
            e.printStackTrace();
        }
    }

    private void rellenarListView(){
        ObservableList<String> listaContra_O_L = FXCollections.observableArrayList();//= datosSingleton.rellenarO_L();

        ArrayList<Contrasena> arr_Con = datosSingleton.getArrayContrasenas();
        ArrayList<String> arr_Stri = new ArrayList<>();
        for(int i=0; i<arr_Con.size(); i++){
            if(arr_Con != null){
                arr_Stri.add(arr_Con.get(i).getTitulo());
            }
        }

        listaContra_O_L.addAll(arr_Stri);
        listViewLista.setItems(listaContra_O_L);
    }



}

