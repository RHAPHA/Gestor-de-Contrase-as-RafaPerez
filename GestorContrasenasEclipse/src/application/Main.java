package application;


import controllers.DatosSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modelo.ClaseAccesoria;
import modelo.Contrasena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {
	
	
    private DatosSingleton datosSingleton;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/vistas/LoginYPassword.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ventana Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        datosSingleton = DatosSingleton.getInstancia();
       // System.out.println("ADIOS");
        if(Objects.nonNull(datosSingleton) && datosSingleton.getCarpetaLogin() !=null ){//si cerramos la aplicación sin escribir login

            //System.out.println("STOP -> datosSingleton.getCarpetaLogin()-> "+datosSingleton.getCarpetaLogin());

           ArrayList<Contrasena> arrayCon =datosSingleton.getArrayContrasenas();
            String login = datosSingleton.getCarpetaLogin();
            File f = new File("."+File.separator+ login +File.separator+"contrasenia2.rpz");
            try {
                boolean creado = f.createNewFile();
               System.out.println("creado "+creado);
                ClaseAccesoria.escribirEnfichero(f, arrayCon);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            /*System.out.println("datosSingleton.getCarpetaLogin())->"+datosSingleton.getCarpetaLogin()+
                    "datosSingleton.getArrayContrasenas() !=null ->"+datosSingleton.getArrayContrasenas() !=null);*/

        }

    }
}


