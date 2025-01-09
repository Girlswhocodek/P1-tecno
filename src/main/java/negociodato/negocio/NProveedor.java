/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.negocio;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.DProveedor;
import negociodato.dato.DUsuario;

/**
 *
 * @author RAMIREZ PINEDA
 */
public class NProveedor {
    
    private final DProveedor dProveedor;
    
    public NProveedor() {
       
        this.dProveedor = new DProveedor();
    }
    
   public void guardar(List<String> parametros) throws SQLException {
    try {
        // Conversión segura de teléfono
        Integer telefono = Integer.parseInt(parametros.get(3));
        dProveedor.guardar(parametros.get(0), parametros.get(1), parametros.get(2), telefono);
    } catch (NumberFormatException e) {
        System.err.println("El valor del teléfono no es un número válido: " + parametros.get(2));
        throw new SQLException("Error al convertir el número de teléfono.", e);
    } finally {
        dProveedor.desconectar();
    }
}
    public void modificar(List<String> parametros) throws SQLException, ParseException {

       int idProvedor = dProveedor.getIdByEmail(parametros.get(1));
        int idUsuarioParametro = Integer.parseInt(parametros.get(0)); //id
        
        // verifica que el email este disponible
        if (idProvedor == -1 || (idProvedor == idUsuarioParametro)) {
            
            dProveedor.modificar(idUsuarioParametro, parametros.get(1),parametros.get(2),parametros.get(3),parametros.get(4));
        }
        
       // dUsuario.desconectar();
        dProveedor.desconectar();
    }
    
    public void eliminar(List<String> parametros) throws SQLException {
        dProveedor.eliminar(Integer.parseInt(parametros.get(0)));
      //  dUsuario.eliminar(Integer.parseInt(parametros.get(0)));
        
       // dUsuario.desconectar();
        dProveedor.desconectar();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> proveedores = (ArrayList<String[]>) dProveedor.listar();
        dProveedor.desconectar();
        return proveedores;
    }
    
}
