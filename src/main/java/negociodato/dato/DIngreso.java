/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.dato;

import database.DBConeccion;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dell
 */
public class DIngreso {
      public static final String[] HEADERS
            = {"ID", "FECHA_INGRESO", "TOTAL_MONTO", "PROVEEDOR"};

    private final DBConeccion connection;

    public DIngreso() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }
    
    // Método para listar todos los ingresos
     public List<String[]> listar() throws SQLException {
        List<String[]> ordenes = new ArrayList<>();
        String query = "SELECT p.name AS proveedor_nombre, i.* FROM proveedors p INNER JOIN ingresos i ON p.id = i.proveedor_id";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] orden = {
                        rs.getString("id"),
                        rs.getString("fecha_ingreso"),
                        rs.getString("total"),
                        rs.getString("proveedor_nombre"),
                    };
                    ordenes.add(orden);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de órdenes: " + e.getMessage());
            throw e;
        }

        return ordenes;
    }


    // Método para agregar un nuevo ingreso
    public void guardar(
            int idProveedor) throws SQLException, ParseException {
        

        String query = "INSERT INTO ingresos(proveedor_id, total, fecha_ingreso, created_at, updated_at) VALUES (?, 0, CURRENT_TIMESTAMP,  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, idProveedor);

            int result = ps.executeUpdate();
            if (result == 0) {
                System.err.println("Error al insertar un ingreso en la base de datos.");
                throw new SQLException("Error al insertar un ingreso en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la operación: " + e.getMessage());
            throw e;
        }
    }
    
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }

}
