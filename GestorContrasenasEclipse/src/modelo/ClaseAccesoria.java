package modelo;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class ClaseAccesoria {

    public static void Dialogo(String mensaje, Alert.AlertType tipo) {
        Alert dialogo = new Alert(tipo);
        dialogo.setTitle(seleccionarTitulo(tipo));
        dialogo.setHeaderText(mensaje);
        dialogo.showAndWait();
    }

    private static String seleccionarTitulo(Alert.AlertType tipo) {
        String cadena = "";
        switch (tipo) {
            case INFORMATION : {
                cadena = "Información";
                break;
            }
            case WARNING : {
                cadena = "Atención";
                break;
            }
            case ERROR : {
                cadena = "Error";
                break;
            }
            default : {
            }
        }
        return cadena;
    }

    public static void escribirEnfichero(File fichero, Object objeto) {
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

    public static boolean cumpleExpresionRegular(String patron, String cadenaEvaluada) {
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(cadenaEvaluada);
        return matcher.matches();
    }

    public static void imprirArray(ArrayList<Contrasena> arr) {

        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i).toString());
        }
    }
}
