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
public class DProveedor {
    public static final String[] HEADERS = {"ID", "NOMBRE", "DIRECCION","TELEFONO", "CORREO"};

    private final DBConeccion connection;

    public DProveedor() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   
    }
    
   public void guardar(String nombre,  String correo,String direccion, Integer telefono) throws SQLException {
    String query = "INSERT INTO proveedors(name, email, direccion, telefono, created_at, updated_at) "
                 + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setString(1, nombre);
        ps.setString(2, correo);
        ps.setString(3, direccion);
        ps.setInt(4, telefono);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DProveedor.java dice: "
                             + "Ocurrió un error al insertar un proveedor guardar()");
            throw new SQLException("No se insertó ningún registro.");
        }
    }
}

    public void modificar(int id,String nombre, String direccion, String telefono, String correo) throws SQLException, ParseException {
        
        String query = "UPDATE proveedors SET name=?, direccion=?, telefono=?, email=? WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre);
        ps.setString(2, direccion);
        ps.setString(3, telefono);
        ps.setString(4, correo);
        ps.setInt(5, id);

        if (ps.executeUpdate() == 0) {
            System.err.println("Class DProveedor.java dice: "
                    + "Ocurrio un error al modificar un proveedor modificar()");
            throw new SQLException();
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM proveedors WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);
        if (ps.executeUpdate() == 0) {
            System.err.println("Class DProveedor.java dice: "
                    + "Ocurrio un error al eliminar un proveedor eliminar()");
            throw new SQLException();
        }
    }

    public List<String[]> listar() throws SQLException {
        List<String[]> proveedores = new ArrayList<>();
        String query = "SELECT * FROM proveedors";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            proveedores.add(new String[]{
                String.valueOf(set.getInt("id")),
                set.getString("name"),
                set.getString("direccion"),
                set.getString("telefono"),
                set.getString("email"),
            });
        }
        return proveedores;
    }

    public String[] ver(int id) throws SQLException {
        String[] usuario = null;
        String query = "SELECT * FROM proveedors WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            usuario = new String[]{
                 String.valueOf(set.getInt("id")),
                set.getString("nombre"),
                set.getString("telefono"),
                set.getString("direccion"),
                set.getString("correo"),
            };
        }
        return usuario;
    }

    public int getIdByEmail(String email) throws SQLException {
        int id = -1;
        String query = "SELECT * FROM proveedors WHERE correo=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, email);

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
