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
public class NMedotoValuacions {
    
    private final DMetodoValuacion dMetodoValuacion;
    
    public NMedotoValuacions() {
     
        this.dMetodoValuacion = new DMetodoValuacion();
    }
    
     public void guardar(List<String> parametros) throws SQLException {
         
        int idMetodoValuacion = dMetodoValuacion.getIdByNombre(parametros.get(0));
        
        // verifica que el email se encuentra disponible
         if (idMetodoValuacion == -1) {
            dMetodoValuacion.guardar(parametros.get(0));
            
         }

        //dUsuario.desconectar();
        dMetodoValuacion.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {

       int idMetodoValuacion = dMetodoValuacion.getIdByNombre(parametros.get(1));
        int idParametro = Integer.parseInt(parametros.get(0)); //id
        
        // verifica que el email este disponible
        if (idMetodoValuacion == -1 || (idMetodoValuacion == idParametro)) {
            
            dMetodoValuacion.modificar(idParametro, parametros.get(1));
        }
        
       // dUsuario.desconectar();
        dMetodoValuacion.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dMetodoValuacion.eliminar(Integer.parseInt(parametros.get(0)));

        dMetodoValuacion.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> MetodoValuacions = (ArrayList<String[]>) dMetodoValuacion.listar();
        dMetodoValuacion.desconectar();
        return MetodoValuacions;
    }
    
}
