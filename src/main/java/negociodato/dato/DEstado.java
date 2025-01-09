/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RAMIREZ PINEDA
 */
public class DEstado {
    public static final String[] HEADERS = {"ID", "NOMBRE"};

    private final DBConeccion connection;

    public DEstado() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   
    }
    
    public void guardar(String nombre) throws SQLException {

        String query = "INSERT INTO estados(nombre)"
                + " values(?)";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre.toUpperCase());

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DEstado.java dice: "
                    + "Ocurrio un error al insertar un estado guardar()");
            throw new SQLException();
        }
    }

    public void modificar(int id,String nombre) throws SQLException, ParseException {
        
        String query = "UPDATE estados SET nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre.toUpperCase());
        ps.setInt(5, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DEstado.java dice: "
                    + "Ocurrio un error al modificar un estado modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM estados WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DEstado.java dice: "
                    + "Ocurrio un error al eliminar un estado eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> estados = new ArrayList<>();
        String query = "SELECT * FROM estados";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            estados.add(new String[]{
                String.valueOf(set.getInt("id")),
                set.getString("nombre"),
            });
        }
        return estados;
    }

    public String[] ver(int id) throws SQLException {
        String[] estado = null;
        String query = "SELECT * FROM estados WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            estado = new String[]{
                 String.valueOf(set.getInt("id")),
                set.getString("nombre"),
            };
        }
        return estado;
    }

    public int getIdByNombre(String nombre) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM estados WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            id = set.getInt("id");
        }
        return id;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
