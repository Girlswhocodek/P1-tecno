/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negociodato.negocio.*;


/**
 *
 * @author dell
 */
public class Negocio {
      public static void main(String[] args) {

       //usuario();
       //cliente();
       //proveedor();
       //estado();
       //categoria();
       producto();
       //orden();
       //detalleorden();
    }

    public static void usuario() {
        NUsuario nUsuario = new NUsuario();
        List<String> us = new ArrayList<>();
        us.add("Usuario Prueba 01");
        us.add("testapp1715@gmail.com");
        us.add("password");
        us.add("admin");
        
        try {

            nUsuario.guardar(us);

            ArrayList<String[]> listaUsuarios = nUsuario.listar();
            for (int i = 0; i < listaUsuarios.size(); i++) {
                String[] usuarioActual = listaUsuarios.get(i);
                System.out.println(Arrays.toString(usuarioActual));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void cliente() {
        NCliente nCliente = new NCliente();
        List<String> us = new ArrayList<>();
        us.add("Cliente 01");
        us.add("Direccion 01");
        us.add("79489076");
        us.add("cliente01@gmail.com");
        us.add("981577");
        us.add("M");
        
        try {

            nCliente.guardar(us);

            ArrayList<String[]> listaUsuarios = nCliente.listar();
            for (int i = 0; i < listaUsuarios.size(); i++) {
                String[] usuarioActual = listaUsuarios.get(i);
                System.out.println(Arrays.toString(usuarioActual));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public static void proveedor() {
        NProveedor nProveedor = new NProveedor();
        List<String> us = new ArrayList<>();
        us.add("Cliente 01");
        us.add("Direccion 01");
        us.add("79489076");
        us.add("cliente01@gmail.com");;
        
        try {

            nProveedor.guardar(us);

            ArrayList<String[]> listaUsuarios = nProveedor.listar();
            for (int i = 0; i < listaUsuarios.size(); i++) {
                String[] usuarioActual = listaUsuarios.get(i);
                System.out.println(Arrays.toString(usuarioActual));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public static void estado() {
        NEstado nEstado = new NEstado();
        List<String> us = new ArrayList<>();
        us.add("Pedido");
        
        try {

            nEstado.guardar(us);

            ArrayList<String[]> listar = nEstado.listar();
            for (int i = 0; i < listar.size(); i++) {
                String[] usuarioActual = listar.get(i);
                System.out.println(Arrays.toString(usuarioActual));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public static void categoria() {
        NCategoria nCategoria = new NCategoria();
        List<String> us = new ArrayList<>();
        us.add("Frutas");
        
        try {

            nCategoria.guardar(us);

            ArrayList<String[]> listar = nCategoria.listar();
            for (int i = 0; i < listar.size(); i++) {
                String[] usuarioActual = listar.get(i);
                System.out.println(Arrays.toString(usuarioActual));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void producto() {
        List<String> producto = new ArrayList<>();
//        usuario.add("3");
       /* producto.add("PALTA");
        producto.add("PALTAS");
        producto.add("Productos registrado de prueba");
        producto.add("10.00");
        producto.add("Frutas");*/
      //  producto.add("3");

        try {
            NProducto nProducto = new NProducto();
           // nProducto.modificar(producto);
//          nUsuario.modificar(usuario);       
//          nUsuario.eliminar(usuario);

            ArrayList<String[]> listaProductos = nProducto.listar();

            for (int i = 0; i < listaProductos.size(); i++) {
                String[] productoActual = listaProductos.get(i);
                System.out.println(Arrays.toString(productoActual));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public static void orden() {
        List<String> orden = new ArrayList<>();
//        usuario.add("3");
        orden.add("1"); //comentar para guardar, descomentar para modificar //
        orden.add("00.00");
        orden.add("2");
        orden.add("4");


        try {
            NOrden nOrden = new NOrden();
//          nOrden.guardar(orden);
          nOrden.modificar(orden);       
//          nOrden.eliminar(orden);

            ArrayList<String[]> listaOrden = nOrden.listar();

            for (int i = 0; i < listaOrden.size(); i++) {
                String[] productoActual = listaOrden.get(i);
                System.out.println(Arrays.toString(productoActual));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      public static void detalleorden() {
        List<String> orden = new ArrayList<>();
//        usuario.add("3");
        orden.add("1"); //comentar para guardar, descomentar para modificar //
        orden.add("3");
        orden.add("2");
       

        try {
           NDetalleOrden nDetalleOrden = new NDetalleOrden();
          nDetalleOrden.guardar(orden);
//          nDetalleOrden.modificar(orden);       
//          nDetalleOrden.eliminar(orden);

   /*         ArrayList<String[]> listaOrden = nDetalleOrden.listar();

            for (int i = 0; i < listaOrden.size(); i++) {
                String[] productoActual = listaOrden.get(i);
                System.out.println(Arrays.toString(productoActual));
            }
*/
        } catch (SQLException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Negocio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
