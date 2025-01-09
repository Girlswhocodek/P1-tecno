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
public class NEstado {
    
    private final DEstado dEstado;
    
    public NEstado() {
     
        this.dEstado = new DEstado();
    }
    
     public void guardar(List<String> parametros) throws SQLException {
         
        int idEstado = dEstado.getIdByNombre(parametros.get(0));
        
        // verifica que el email se encuentra disponible
         if (idEstado == -1) {
            dEstado.guardar(parametros.get(0));
            
         }

        //dUsuario.desconectar();
        dEstado.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {

       int idEstado = dEstado.getIdByNombre(parametros.get(1));
        int idParametro = Integer.parseInt(parametros.get(0)); //id
        
        // verifica que el email este disponible
        if (idEstado == -1 || (idEstado == idParametro)) {
            
            dEstado.modificar(idParametro, parametros.get(1));
        }
        
       // dUsuario.desconectar();
        dEstado.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dEstado.eliminar(Integer.parseInt(parametros.get(0)));

        dEstado.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> estados = (ArrayList<String[]>) dEstado.listar();
        dEstado.desconectar();
        return estados;
    }
    
}
