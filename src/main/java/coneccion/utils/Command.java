/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coneccion.utils;

/**
 *
 * @author Alison
 */
public class Command {
    private static final String END = "\r\n"; //lo declaramos para el salto de liea

    //COMANDO USER user
    public static String user(String user) {
        return "USER " + user + END;
    }
    //comando PASS password
    public static String pass(String pass) {
        return "PASS " + pass + END;
    }
    //comando STAT
    public static String stat() {
        return "STAT " + END;
    }
    //comando LIST para listar los emails
    public static String list() {
        return "LIST " + END;
    }
    //comando QUIT para salir o terminar la coneccion
    public static String quit(){
        return "QUIT" + END;
    }
    //comando RETR para mostrar el email segun el numero de email en la lista
    public static String retr(int email){
        return "RETR " + email + END;
    }
    //comando DELE para eliminar un email segun el nro
    public static String dele(int email){
        return "DELE " + email + END;
    }
}
