package modelo;


import java.io.UnsupportedEncodingException;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
//import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import static javafx.scene.control.Alert.AlertType.ERROR;

public class Encriptacion {


    private static String LLAVE = "NiñoAmosAlTurron";//"SomosProgramadores";

    public static String pruebaSha256 (String cadena) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("sha-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte [] cifrado = md.digest(cadena.getBytes());
        //System.out.println(cifrado.toString());
        StringBuffer ch = new StringBuffer();
        for ( int i =0; i<cifrado.length;i++){
            ch.append(Integer.toHexString(0xFF & cifrado[i]));
        }

       // System.out.println("La cadena en Sha256 es: " + ch.toString());
        return ch.toString();

    }



    /**
     * Este metodo lleva a cabo la encriptacion
     *
     * @param secretKey
     * @param Clave
     * @return
     */
    public static String codificaContrasenia(String secretKey, String Clave) {

        String encriptacion = "";
        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");//SHA-512
            byte[] llavePassword = md5.digest(secretKey.getBytes("UTF-8"));
            byte[] bytesKey = Arrays.copyOf(llavePassword, 24);

            SecretKey key = new SecretKeySpec(bytesKey, "DESede");
            Cipher cifrador = Cipher.getInstance("DESede");

            cifrador.init(Cipher.ENCRYPT_MODE, key);

            byte[] arrayBitesClave = Clave.getBytes("UTF-8");
            byte[] arrayFinal = cifrador.doFinal(arrayBitesClave);
            byte[] arraybase64Bytes = Base64.encodeBase64(arrayFinal);

            encriptacion = new String(arraybase64Bytes);

        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "ERROR: "+ex.getMessage()+
            //  "\n"+ ex.toString());
            ex.printStackTrace();
        }
        return encriptacion;
    }

    /**
     *
     * @param clave
     * @param cadenaEncriptada
     * @return Devuelve la cadena desencriptada.
     */
    public static String descodificacionContrasenia(String clave, String cadenaEncriptada) {

        String desencriptacion = "";

        try {
            byte[] arraydescodificacion = Base64.decodeBase64(cadenaEncriptada.getBytes("UTF-8"));
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] arrayDigest = md5.digest(clave.getBytes("UTF-8"));
            byte[] bytesKey = Arrays.copyOf(arrayDigest, 24);

            SecretKey key = new SecretKeySpec(bytesKey, "DESede");
            Cipher descifrador = Cipher.getInstance("DESede");

            descifrador.init(Cipher.DECRYPT_MODE, key);

            byte[] arrayClaveDescrip = descifrador.doFinal(arraydescodificacion);

            desencriptacion = new String(arrayClaveDescrip, "UTF-8");

        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage() + "\n" + ex.toString());
            ex.printStackTrace();
        }
        return desencriptacion;
    }

    /**
     * Clave de encriptacion desencriptacion
     *
     * @param llave
     * @return
     */
    public static SecretKeySpec crearClave(String llave) {

        try {
            byte[] cadena = llave.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            cadena = md.digest(cadena);
            cadena = Arrays.copyOf(cadena, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES");
            return secretKeySpec;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String Encriptar(String claveAEncriptar) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

        try {
            SecretKeySpec sks = crearClave(LLAVE);


            String str = new String(sks.getEncoded(), java.nio.charset.StandardCharsets.UTF_8);
           // System.out.println("SecretKeySpec  "+sks.toString()+ " "+str);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);

            byte[] arrayBytes = claveAEncriptar.getBytes("UTF-8");
            byte[] cadenaEncriptada = cipher.doFinal(arrayBytes);
            byte[] cadenaEncriptadaByte = Base64.encodeBase64(cadenaEncriptada);

            String cadenaEncriptadaStr = new String(cadenaEncriptadaByte);

            return cadenaEncriptadaStr;

        } catch (UnsupportedEncodingException ex) {
            ClaseAccesoria.Dialogo("Error1: "+ex.getMessage(), ERROR);

        } catch (IllegalBlockSizeException ex) {
            ClaseAccesoria.Dialogo("Error2: "+ex.getMessage(), ERROR);

        } catch (BadPaddingException ex) {
            ClaseAccesoria.Dialogo("Error3: "+ex.getMessage(), ERROR);
        }
        return null;

    }

    public static String Des_Encriptar(String claveA_Des_Encriptar) {

        try {
            SecretKeySpec sks = crearClave(LLAVE);

            String str = new String(sks.getEncoded(), java.nio.charset.StandardCharsets.UTF_8);
            //System.out.println("SecretKeySpec  "+sks.toString()+ " "+str);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);

            byte[] arrayBytes = Base64.decodeBase64(claveA_Des_Encriptar);
            byte[] Des_Encriptada = cipher.doFinal(arrayBytes);

            String cadena_Des_Encriptada = new String(Des_Encriptada);

            return cadena_Des_Encriptada;

        } catch (IllegalBlockSizeException ex) {
            ClaseAccesoria.Dialogo("Error1: "+ex.getMessage(), ERROR);
        } catch (BadPaddingException ex) {
            ClaseAccesoria.Dialogo("Error2: "+ex.getMessage(), ERROR);
        } catch (NoSuchAlgorithmException ex) {
            ClaseAccesoria.Dialogo("Error3: "+ex.getMessage(), ERROR);
        } catch (NoSuchPaddingException ex) {
            ClaseAccesoria.Dialogo("Error4: "+ex.getMessage(), ERROR);
        } catch (InvalidKeyException ex) {
            ClaseAccesoria.Dialogo("Error5: "+ex.getMessage(), ERROR);
        }
        return null;

    }

}
