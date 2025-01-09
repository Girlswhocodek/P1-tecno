/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interpreter;

/**
 *
 * @author dell
 */

import interpreter.analex.Interpreter;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.events.TokenEvent;
import interpreter.analex.utils.Token;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import negociodato.negocio.NUsuario;
import negociodato.negocio.NComando;


public class MainTest {

    public static void main(String[] args) {

        // producto agregar [Clean Code; Libro de codigo limpio; Robert C. Martin; url de la imagen; 50; 80; Horror; Navidad]
        // pago agregar [tigo money; 2023-11-25; 13:30; venta de producto; 1]
        // inventario agregar [libro de programacion; 1; 1]
        String comando = "USUARIO AGREGAR [AB;PZA@test.com;Radial #13;cliente]";
//        String correo = "grupo07sa@tecnoweb.org.bo";
        String correo = "testapp1715@gmail.com";
    
        NComando nComando = new NComando();
        NUsuario nUsuario = new NUsuario();

        Interpreter interpreter = new Interpreter(comando, correo);

        interpreter.setListener(new ITokenEventListener() {

            @Override
            public void usuario(TokenEvent event) {
                System.out.println("CU: USUARIO");
                System.out.println(event);
                try {
                    switch (event.getAction()) {
                        case Token.AGREGAR:
                            nUsuario.guardar(event.getParams());
                            System.out.println("OK");
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
                            for (int i = 0; i < lista.size(); i++) {
                                s = s + "[" + i + "] : ";
                                for (int j = 0; j < lista.get(i).length; j++) {
                                    s = s + lista.get(i)[j] + " | ";
                                }
                                s = s + "\n";
                            }
                            System.out.println(s);
                            break;
                        default:
                            System.out.println("La accion no es valida para el caso de uso");
                            //enviar al correo una notificacion
                            break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Mensaje: " + ex.getSQLState());
                    //enviar notificacion de error
                //} catch (ParseException ex) {
                    //Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                } catch (javax.mail.internet.ParseException ex) {
                    Logger.getLogger(MainTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

         
//        
            @Override
            public void error(TokenEvent event) {
                System.out.println("ocurrio un error");
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void help(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

//       


            @Override
            public void producto(TokenEvent event) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void cliente(TokenEvent event) {
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

