/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.negocio;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.*;


/**
 *
 * @author RAMIREZ PINEDA
 */
public class NCategoria {
    
    private final DCategoria dCategoria;
    
    public NCategoria() {
     
        this.dCategoria = new DCategoria();
    }
    
     public void guardar(List<String> parametros) throws SQLException {
         
        int idCategoria = dCategoria.getIdByNombre(parametros.get(0));
        
        // verifica que el email se encuentra disponible
         if (idCategoria == -1) {
            dCategoria.guardar(parametros.get(0));
            
         }

        //dUsuario.desconectar();
        dCategoria.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {

       int idCategoria = dCategoria.getIdByNombre(parametros.get(1));
        int idParametro = Integer.parseInt(parametros.get(0)); //id
        
        // verifica que el email este disponible
        if (idCategoria == -1 || (idCategoria == idParametro)) {
            
            dCategoria.modificar(idParametro, parametros.get(1));
        }
        
       // dUsuario.desconectar();
        dCategoria.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dCategoria.eliminar(Integer.parseInt(parametros.get(0)));

        dCategoria.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> categorias = (ArrayList<String[]>) dCategoria.listar();
        dCategoria.desconectar();
        return categorias;
    }
    
}
