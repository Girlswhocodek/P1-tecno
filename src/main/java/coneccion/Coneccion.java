/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package coneccion;

import coneccion.comunicacion.ClientePOP;
import coneccion.comunicacion.ClienteSMTP;
import coneccion.interfaces.IEmailEventListener;
import coneccion.utils.Email;
import interpreter.MainTest;
import interpreter.analex.Interpreter;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.analex.utils.Token;
import interpreter.events.TokenEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.ParseException;
import negociodato.negocio.NUsuario;

/**
 *
 * @author dell
 */
public class Coneccion {
     public static void main(String[] args) {
        System.out.println("hello world");
        
       /* ClientePOP mail = new ClientePOP();
        mail.setEmailEventListener(new IEmailEventListener() {
            @Override
            public void onReceiveEmailEvent(List<Email> emails) {
                for (Email email : emails) {
                    System.out.println(email);
                    interpreter(email);
                }
            }
        });
        
        Thread thread = new Thread((Runnable) mail);
        thread.setName("Mail Verification Thread");
        thread.start();
        
        
     */ 
       
                 
      Email emailObject = new Email("testapp1715@gmail.com", Email.SUBJECT,
                "Petición realizada correctamente");

        ClienteSMTP sendEmail = new ClienteSMTP(emailObject);
        Thread thread = new Thread(sendEmail);
        thread.setName("Send email Thread");
        thread.start();
    }
    
   //interpreter
    

    
    public static void interpreter(Email email ){
        // comandos

         // genero agregar [Horror]
        // promocion agregar [Navidad, 30]
        // producto agregar [Clean Code; Libro de codigo limpio; Robert C. Martin; url de la imagen; 50; 80; Horror; Navidad]
        // producto modificar [1; Clean Code; Libro de editado; Robert C Martin; url de la imagen; 50; 80; Horror; Navidad]
        
//        NGenero nGenero = new NGenero();
//        NPromocion nPromocion = new NPromocion();
        NUsuario nUsuario = new NUsuario();
        
        String sender = email.getFrom() + " ";
        Interpreter interpreter = new Interpreter(email.getSubject(), sender);  
        
        interpreter.setListener(new ITokenEventListener() {


            @Override
            public void usuario(TokenEvent event) {
                System.out.println("CU: PRODUCTO");
                System.out.println(event);
                try {
                    switch (event.getAction()) {
                        case Token.AGREGAR:
                            nUsuario.guardar(event.getParams());
                            System.out.println("OK");
                            //notificar al usuario que se ejecuto su comando
                            break;
                        case Token.MODIFICAR:
                            nUsuario.modificar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.ELIMINAR:
                            nUsuario.eliminar(event.getParams());
                            System.out.println("OK");
                            break;
                        case Token.LISTAR:
                            ArrayList<String[]> lista = nUsuario.listar();
                            String s = "";
                            for(int i = 0; i < lista.size(); i++) {
                                s = s + "["+i+"] : ";
                                for(int j = 0; j <lista.get(i).length; j++) {
                                    s = s + lista.get(i)[j] + " | ";
                                }
                                s = s + "\n";
                            }   System.out.println(s);
                            break;
                        default:                
                            System.out.println("La accion no es valida para el caso de uso");
                            //enviar al correo una notificacion
                            break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Mensaje: "+ex.getSQLState());
                    //enviar notificacion de error
                } catch (ParseException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }


            @Override
            public void error(TokenEvent event) {
                System.out.println("ocurrio un error");
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void help(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void cliente(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void producto(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }


            @Override
            public void orden(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void detalleorden(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void ingreso(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void proveedor(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void detalleingreso(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void categoria(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void estado(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void pago(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            
        });
        
        
        Thread thread = new Thread(interpreter);
        thread.setName("Interpreter Thread");
        thread.start();
    }
   
}
