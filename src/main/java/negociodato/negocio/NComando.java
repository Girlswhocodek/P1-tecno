/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import negociodato.dato.DComando;

/**
 *
 * @author dell
 */
public class NComando {
       private DComando dComando;
    
    public NComando() {
        this.dComando = new DComando();
    }
    
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> comandos = (ArrayList<String[]>) dComando.listar();
        dComando.desconectar();
        return comandos;
    }
}
