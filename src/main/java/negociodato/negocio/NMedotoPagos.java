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
public class NMedotoPagos {
    
    private final DMetodoPago dMetodoPago;
    
    public NMedotoPagos() {
     
        this.dMetodoPago = new DMetodoPago();
    }
    
     public void guardar(List<String> parametros) throws SQLException {
         
        int idMetodoPago = dMetodoPago.getIdByNombre(parametros.get(0));
        
        // verifica que el email se encuentra disponible
         if (idMetodoPago == -1) {
            dMetodoPago.guardar(parametros.get(0));
            
         }

        //dUsuario.desconectar();
        dMetodoPago.desconectar();
    }
    
    public void modificar(List<String> parametros) throws SQLException, ParseException {

       int idMetodoPago = dMetodoPago.getIdByNombre(parametros.get(1));
        int idParametro = Integer.parseInt(parametros.get(0)); //id
        
        // verifica que el email este disponible
        if (idMetodoPago == -1 || (idMetodoPago == idParametro)) {
            
            dMetodoPago.modificar(idParametro, parametros.get(1));
        }
        
       // dUsuario.desconectar();
        dMetodoPago.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dMetodoPago.eliminar(Integer.parseInt(parametros.get(0)));

        dMetodoPago.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> MetodoPagos = (ArrayList<String[]>) dMetodoPago.listar();
        dMetodoPago.desconectar();
        return MetodoPagos;
    }
    
}
