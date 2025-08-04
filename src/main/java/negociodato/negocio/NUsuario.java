/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.negocio;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.mail.internet.ParseException;
import negociodato.dato.DUsuario;

/**
 *
 * @author dell
 */
public class NUsuario {
    private final DUsuario dUsuario;

    public NUsuario() {
        this.dUsuario = new DUsuario();
    }

    public void getIdByEmail(String email) throws SQLException {
        if (email.isEmpty()) {
            throw new SQLException("Parametros vacios!");
        }

    }
    
    public void getUsserById(String email) throws SQLException {
        if (email.isEmpty()) {
            throw new SQLException("Parametros vacios!");
        }

    }

    public int guardar(List<String> parametros) throws SQLException {
        if (parametros.isEmpty()) {
            throw new SQLException("Parametros vacios!");
        }
        int IdNewUser=dUsuario.guardar(
                parametros.get(0),
                parametros.get(1),
                parametros.get(2)
        );
        dUsuario.desconectar();
        return  IdNewUser;
    }

 public String[] modificar(List<String> parametros) throws SQLException, ParseException {
    // Verificar que el email proporcionado sea único
    //int idUsuario = dUsuario.getIdByEmail(parametros.get(2)); // Cambiar el índice según el email
    int idUsuarioParametro = Integer.parseInt(parametros.get(0)); // ID del usuario


        // Si el ID de usuario es el mismo o no se encuentra el email, simplemente actualiza
       String[] User= dUsuario.modificar(idUsuarioParametro, parametros.get(1),
                parametros.get(2));
    

    dUsuario.desconectar();
    return User;
}

    public void eliminar(List<String> parametros) throws SQLException {
        dUsuario.eliminar(Integer.parseInt(parametros.get(0)));
        dUsuario.desconectar();
    }

     public String[] ver(int idUser) throws SQLException {
        String[] usuario = (String[]) dUsuario.ver(idUser);
        dUsuario.desconectar();
        return usuario;
    }

    public NUsuario(DUsuario dUsuario) {
        this.dUsuario = dUsuario;
    }
    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> usuarios = (ArrayList<String[]>) dUsuario.listar();
        dUsuario.desconectar();
        return usuarios;
    }
}
