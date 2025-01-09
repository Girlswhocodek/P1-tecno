/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.negocio;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.DIngreso;

/**
 *
 * @author dell
 */
public class NIngreso {
      private final DIngreso dIngreso;

    public NIngreso() {
        this.dIngreso = new DIngreso();
    }

    public void guardar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        
        dIngreso.guardar( 
                Integer.parseInt(parametros.get(0))
        );

        dIngreso.desconectar();
    }
     public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> productos = (ArrayList<String[]>) dIngreso.listar();
        dIngreso.desconectar();
        return productos;
    }
}
