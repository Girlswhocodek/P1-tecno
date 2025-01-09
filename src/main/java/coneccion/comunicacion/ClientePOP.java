/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coneccion.comunicacion;

import coneccion.interfaces.IEmailEventListener;
import coneccion.utils.Command;
import coneccion.utils.Email;
import coneccion.utils.Extractor;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.sasl.AuthenticationException;

/**
 *
 * @author dell
 */
public class ClientePOP implements Runnable{
    
    static final String HOST = "mail.tecnoweb.org.bo";
    static final int PORT_POP=110;
    public static String USER="grupo18sa";
    public static String PASSWORD="grup018grup018*";
    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    private IEmailEventListener emailEventListener; 
   


    public IEmailEventListener getEmailEventListener() {
        return emailEventListener;
    }

    public void setEmailEventListener(IEmailEventListener emailEventListener) {
        this.emailEventListener = emailEventListener;
    }
    
    public ClientePOP() {
        socket = null;
        input = null;
        output = null;
    }

    @Override
    public void run() {
        while(true) {
            try {
                List<Email> emails = null;
                socket = new Socket(HOST, PORT_POP);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new DataOutputStream(socket.getOutputStream());
                System.out.println("**************** Conexion establecida *************");
                
                authUser(USER, PASSWORD);
                
                int count = getEmailCount();
                if(count > 0 ) {
                    emails = getEmails(count);
                    System.out.println(emails);
                  
                }
                output.writeBytes(Command.quit());
                input.readLine();
                input.close();
                output.close();
                socket.close();
                System.out.println("************** Conexion Terminada ************");
                
                if(count > 0) {
                   emailEventListener.onReceiveEmailEvent(emails);
                }
                
                Thread.sleep(10000);
                
            } catch (IOException ex) {
                Logger.getLogger(ClientePOP.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientePOP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void authUser(String email, String password) throws IOException {
        if(socket != null && input != null && output != null) {
            input.readLine();
            output.writeBytes(Command.user(email));
            input.readLine();
            output.writeBytes(Command.pass(password));
            String message = input.readLine();
            if(message.contains("-ERR")) {
                throw new AuthenticationException();
            }
        }
    }
    
    private void deleteEmails(int emails) throws IOException {
        for(int i = 1; i <= emails; i++) {
            output.writeBytes(Command.dele(i));
        }
    }
    
    private int getEmailCount() throws IOException {
        output.writeBytes(Command.stat());
        String line = input.readLine();
        String[] data = line.split(" ");        
        return Integer.parseInt(data[1]);
    }
    
    private List<Email> getEmails(int count) throws IOException {
        List<Email> emails = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            output.writeBytes(Command.retr(i));
            String texto = readMultiline();
            emails.add(Extractor.getEmail(texto));
             deleteEmails(i);
        }
        return emails;
    }
    
    private String readMultiline() throws IOException {
        String lines = "";
        while(true) {
            String line = input.readLine();
            if(line == null) {
                throw new IOException("Server no responde (ocurrio un error al abrir el correo)");
            }
            if(line.equals(".")) {
                break;
            }
            lines = lines + "\n" + line;
        }
        return lines;
    }
    
     // Permite leer multiples lineas de respuesta del Protocolo POP
    static protected String getMultiline(BufferedReader in) throws IOException{
        String lines = "";
        while (true){
            String line = in.readLine();
            if (line == null){
               // Server closed connection
               throw new IOException(" S : Server no responde (ocurrio un error al abrir el correo).");
            }
            if (line.equals(".")){
                // No more lines in the server response
                break;
            }
            if ((line.length() > 0) && (line.charAt(0) == '.')){
                // The line starts with a "." - strip it off.
                line = line.substring(1);
            }
            // Add read line to the list of lines
            lines=lines+"\n"+line;
        }       
        return lines;
    }
    
    static protected String lastLine(String in){
        String[] last=in.split("\n");
        return last[last.length-1];
    }
    
    static protected String queryLine(String in){
        String query="";
        int pos=in.indexOf(":")+1;
        if(pos>=0){
            return query=in.substring(pos).trim();
        }else{
            return "El SUBJECT no cuenta con un patron especificado";
        }
    }
    
}
