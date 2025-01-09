/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author dell
 */


import coneccion.comunicacion.ClientePOP;
import coneccion.comunicacion.ClienteSMTP;
import coneccion.interfaces.IEmailEventListener;
import coneccion.utils.Email;
import interpreter.MainTest;
import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.events.TokenEvent;
import java.util.List;

import negociodato.negocio.NUsuario;

import interpreter.analex.Interpreter;
import interpreter.analex.utils.Token;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import negociodato.dato.*;
import negociodato.negocio.*;

public class Aplication implements IEmailEventListener, ITokenEventListener {

    private static final int CONSTRAINTS_ERROR = -2;
    private static final int NUMBER_FORMAT_ERROR = -3;
    private static final int INDEX_OUT_OF_BOUND_ERROR = -4;
    private static final int PARSE_ERROR = -5;
    private static final int AUTHORIZATION_ERROR = -6;

    private ClientePOP mailVerificationThread;


    private NComando nComando;
    private NUsuario nUsuario;
    private NProducto nProducto;
    private NCliente nCliente;
    private NProveedor nProveedor;
    private NCategoria nCategoria;
    private NEstado nEstado;
    private NIngreso nIngreso;
    private NDetalleIngreso nDetalleIngreso;
    private NOrden nOrden;
    private NDetalleOrden nDetalleOrden;
    private NPago nPago;
    
    public Aplication() {
        mailVerificationThread = new ClientePOP();
        mailVerificationThread.setEmailEventListener(Aplication.this);


        nComando = new NComando();
        nUsuario = new NUsuario();
        nProducto= new NProducto();
        nCliente = new NCliente();
        nProveedor = new NProveedor();
        nIngreso = new NIngreso();
        nDetalleIngreso = new NDetalleIngreso();
        nOrden = new NOrden();
        nDetalleOrden = new NDetalleOrden();
    }

    public void start() {
        Thread thread = new Thread(mailVerificationThread);
        thread.setName("Mail Verfication Thread");
        thread.start();
        
    }

    @Override
    public void onReceiveEmailEvent(List<Email> emails) {
        for (Email email : emails) {
            String subject = email.getSubject() + " ";
            Interpreter interpreter = new Interpreter(subject, email.getFrom());
            interpreter.setListener(Aplication.this);
            Thread thread = new Thread(interpreter);
            thread.setName("Interpreter Thread");
            thread.start();
        }
    }

    @Override
    public void help(TokenEvent event) {
        System.out.println("HELP");
        try {
            tableNotifySuccess(event.getSender(), "Lista de Comandos", DComando.HEADERS, nComando.listar());
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

  
    @Override
    public void usuario(TokenEvent event) {
        System.out.println("USUARIO");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nUsuario.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Usuario de  Guardado Correctamente");
                    break;
                case Token.MODIFICAR:
                    nUsuario.modificar(event.getParams());
                    System.out.println("Usuario de  modificado con exito");
                    simpleNotifySuccess(event.getSender(), "Usuario  Modificado Correctamente");
                    break;
                case Token.ELIMINAR:
                    nUsuario.eliminar(event.getParams());
                    System.out.println("Usuario de  eliminado con exito");
                    simpleNotifySuccess(event.getSender(), "Usuario Eliminado Correctamente");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista de Usuarios : ", DUsuario.HEADERS, nUsuario.listar());
                    break;

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (javax.mail.internet.ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void error(TokenEvent event) {
        this.handleError(event.getAction(), event.getSender(), event.getParams());
    }

    private void handleError(int type, String email, List<String> args) {
        Email emailObject = null;

        switch (type) {
            case Token.ERROR_CHARACTER:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Caracter desconocido",
                    "No se pudo ejecutar el comando [" + args.get(0) + "] debido a: ",
                    "El caracter \"" + args.get(1) + "\" es desconocido."
                }));
                break;
            case Token.ERROR_COMMAND:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Comando desconocido",
                    "No se pudo ejecutar el comando [" + args.get(0) + "] debido a: ",
                    "No se reconoce la palabra \"" + args.get(1) + "\" como un comando valido"
                }));
                break;
            case CONSTRAINTS_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Error al interactuar con la base de datos",
                    "Referencia a informacion inexistente"
                }));
                break;
            case NUMBER_FORMAT_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Error en el tipo de parametro",
                    "El tipo de uno de los parametros es incorrecto"
                }));
                break;
            case INDEX_OUT_OF_BOUND_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Cantidad de parametros incorrecta",
                    "La cantidad de parametros para realizar la accion es incorrecta"
                }));
                break;
            case PARSE_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Error al procesar la fecha",
                    "La fecha introducida posee un formato incorrecto"
                }));
                break;
            case AUTHORIZATION_ERROR:
                emailObject = new Email(email, Email.SUBJECT,
                        HtmlBuilder.generateText(new String[]{
                    "Acceso denegado",
                    "Usted no posee los permisos necesarios para realizar la accion solicitada"
                }));
                break;
        }

        this.sendEmail(emailObject);
    }

    private void simpleNotifySuccess(String email, String message) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateText(new String[]{
            "Peticion realizada correctamente",
            message
        }));
        sendEmail(emailObject);
    }

    private void simpleNotify(String email, String title, String topic, String message) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateText(new String[]{
            title, topic, message
        }));
        sendEmail(emailObject);
    }

    private void tableNotifySuccess(String email, String title, String[] headers, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTable(title, headers, data));
        sendEmail(emailObject);
    }

    private void tableNotifySuccess2(String email, String title, String[] headers, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTable(title, headers, data));
        sendEmail(emailObject);
    }

    private void tableGraficaSuccess(String email, String title, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateGrafica(title, data));
        sendEmail(emailObject);
    }

    private void tableGraficaSuccess2(String email, String title, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateGrafica2(title, data));
        sendEmail(emailObject);
    }

    private void tableGraficaSuccess3(String email, String title, ArrayList<String[]> data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateGrafica3(title, data));
        sendEmail(emailObject);
    }

    private void simpleTableNotifySuccess(String email, String title, String[] headers, String[] data) {
        Email emailObject = new Email(email, Email.SUBJECT,
                HtmlBuilder.generateTableForSimpleData(title, headers, data));
        sendEmail(emailObject);
    }

    private void sendEmail(Email email) {
        ClienteSMTP sendEmail = new ClienteSMTP(email);
        Thread thread = new Thread(sendEmail);
        thread.setName("Send email Thread");
        thread.start();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void cliente(TokenEvent event) {
         System.out.println("CLIENTE");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nCliente.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Cliente  Guardado Correctamente");
                    break;
              case Token.MODIFICAR:
                    nCliente.modificar(event.getParams());
                    System.out.println(" Cliente modificado con exito");
                    simpleNotifySuccess(event.getSender(), " Cliente Modificado Correctamente");
                    break;
               case Token.ELIMINAR:
                    nCliente.eliminar(event.getParams());
                    System.out.println("Cliente eliminado con exito");
                    simpleNotifySuccess(event.getSender(), "Cliente Eliminado Correctamente");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DCliente.HEADERS, nCliente.listar());
                    break;

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @Override
    public void proveedor(TokenEvent event) {
         System.out.println("PROVEEDOR");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nProveedor.guardar(event.getParams());
                    System.out.println("Proveedor Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Proveedor  Guardado Correctamente");
                    break;
              case Token.MODIFICAR:
                    nProveedor.modificar(event.getParams());
                    System.out.println(" Proveedor modificado con exito");
                    simpleNotifySuccess(event.getSender(), " Proveedor Modificado Correctamente");
                    break;
               case Token.ELIMINAR:
                    nProveedor.eliminar(event.getParams());
                    System.out.println("Proveedor eliminado con exito");
                    simpleNotifySuccess(event.getSender(), "Proveedor Eliminado Correctamente");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DProveedor.HEADERS, nProveedor.listar());
                    break;

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void producto(TokenEvent event) {
            System.out.println("PRODUCTO");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nProducto.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Guardado Correctamente");
                    break;
                case Token.MODIFICAR:
                    nProducto.modificar(event.getParams());
                    System.out.println("Producto  modificado con exito");
                    simpleNotifySuccess(event.getSender(), "Producto  Modificado Correctamente");
                    break;
               case Token.ELIMINAR:
                    nProducto.eliminar(event.getParams());
                    System.out.println("eliminado con exito");
                    simpleNotifySuccess(event.getSender(), " Eliminado Correctamente");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DProducto.HEADERS, nProducto.listar());
                    break;
                /*case Token.VER:
                    tableNotifySuccess(event.getSender(), "Ver Producto: ", DProducto.HEADERS, nProducto.ver(event.getParams(0)));
                    break;*/

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      @Override
      public void ingreso(TokenEvent event) {
            System.out.println("INGRESO");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nIngreso.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Guardado Correctamente");
                    break;
/*                case Token.MODIFICAR:
                    nIngreso.modificar(event.getParams());
                    System.out.println("Producto  modificado con exito");
                    simpleNotifySuccess(event.getSender(), "Producto  Modificado Correctamente");
                    break;
               case Token.ELIMINAR:
                    nProducto.eliminar(event.getParams());
                    System.out.println("eliminado con exito");
                    simpleNotifySuccess(event.getSender(), " Eliminado Correctamente");
                    break;*/
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DIngreso.HEADERS, nIngreso.listar());
                    break;
                /*case Token.VER:
                    tableNotifySuccess(event.getSender(), "Ver Producto: ", DProducto.HEADERS, nProducto.ver(event.getParams(0)));
                    break;*/

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      @Override
      public void detalleingreso(TokenEvent event) {
            System.out.println("DETALLEINGRESO");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nDetalleIngreso.guardar(event.getParams());
                    System.out.println("DetalleIngreso Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "DetalleIngreso Guardado Correctamente");
                    break;
/*                case Token.MODIFICAR:
                    nIngreso.modificar(event.getParams());
                    System.out.println("Producto  modificado con exito");
                    simpleNotifySuccess(event.getSender(), "Producto  Modificado Correctamente");
                    break;
               case Token.ELIMINAR:
                    nProducto.eliminar(event.getParams());
                    System.out.println("eliminado con exito");
                    simpleNotifySuccess(event.getSender(), " Eliminado Correctamente");
                    break;*/
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DDetalleIngreso.HEADERS, nDetalleIngreso.listar(event.getParams()));
                    break;
                /*case Token.VER:
                    tableNotifySuccess(event.getSender(), "Ver Producto: ", DProducto.HEADERS, nProducto.ver(event.getParams(0)));
                    break;*/

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void orden(TokenEvent event) {
   
         System.out.println("ORDEN");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nOrden.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Cliente  Guardado Correctamente");
                    break;
              case Token.MODIFICAR:
                    nOrden.modificar(event.getParams());
                    System.out.println(" modificado con exito");
                    simpleNotifySuccess(event.getSender(), "Modificado Correctamente");
                    break;
               /*case Token.ELIMINAR:
                    nOrden.eliminar(event.getParams());
                    System.out.println(" eliminado con exito");
                    simpleNotifySuccess(event.getSender(), "Eliminado Correctamente");
                    break;*/
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DOrden.HEADERS, nOrden.listar());
                    break;
                /*case Token.VER:
                    tableNotifySuccess(event.getSender(), "Ver Producto: ", DProducto.HEADERS, nProducto.ver(event.getParams(0)));
                    break;*/

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    @Override
    public void detalleorden(TokenEvent event) {
     
         System.out.println("DETALLEORDEN");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nDetalleOrden.guardar(event.getParams());
                    System.out.println("Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Guardado Correctamente");
                    break;
//              case Token.MODIFICAR:
//                    nDetalleOrden.modificar(event.getParams());
//                    System.out.println("  modificado con exito");
//                    simpleNotifySuccess(event.getSender(), "Modificado Correctamente");
//                    break;
              /* case Token.ELIMINAR:
                    nDetalleOrden.eliminar(event.getParams());
                    System.out.println("Cliente eliminado con exito");
                    simpleNotifySuccess(event.getSender(), "Cliente Eliminado Correctamente");
                    break;*/
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista : ", DDetalleOrden.HEADERS, nDetalleOrden.listar(event.getParams()));
                    break;
                /*case Token.VER:
                    tableNotifySuccess(event.getSender(), "Ver Producto: ", DProducto.HEADERS, nProducto.ver(event.getParams(0)));
                    break;*/

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        } catch (ParseException ex) {
            Logger.getLogger(Aplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void categoria(TokenEvent event) {
        
         System.out.println("CATEGORIA");
        try {
            switch (event.getAction()) {
                case Token.AGREGAR:
                    nCategoria.guardar(event.getParams());
                    System.out.println("Categoria Guardado con exito");
                    simpleNotifySuccess(event.getSender(), "Categoria Guardada Correctamente");
                    break;
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DOrden.HEADERS, nCategoria.listar());
                    break;

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    
    }

    @Override
    public void estado(TokenEvent event) {
        
         System.out.println("ESTADO");
        try {
            switch (event.getAction()) {
                case Token.LISTAR:
                    tableNotifySuccess(event.getSender(), "Lista: ", DOrden.HEADERS, nEstado.listar());
                    break;

            }
        } catch (SQLException ex) {
            handleError(CONSTRAINTS_ERROR, event.getSender(), null);
        } catch (NumberFormatException ex) {
            handleError(NUMBER_FORMAT_ERROR, event.getSender(), null);
        } catch (IndexOutOfBoundsException ex) {
            handleError(INDEX_OUT_OF_BOUND_ERROR, event.getSender(), null);
        }
    }

    @Override
    public void pago(TokenEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}

