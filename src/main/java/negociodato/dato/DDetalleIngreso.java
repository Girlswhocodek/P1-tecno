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
public class DDetalleIngreso {
      public static final String[] HEADERS
            = {"ID", "INGRESO_ID", "PRODUCTO", "CANTIDAD", "PRECIO", "COSTO_TOTAL"};

    private final DBConeccion connection;

    public DDetalleIngreso() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }
    
   public void guardar(int ingresoId, String nombreProducto, int cantidad) throws SQLException {
    // Obtener el productoId utilizando el nombre del producto
    DProducto dProducto = new DProducto();
    int productoId = dProducto.getIdByNombre(nombreProducto);

    // Obtener el precio del producto
    double precioProducto = obtenerPrecioProducto(productoId);

    // Calcular el costo total
    double costoTotal = cantidad * precioProducto;

    String query = "INSERT INTO detalle_ingresos(ingreso_id, producto_id, cantidad, costo_unitario, costo_total, created_at, updated_at) "
                 + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setInt(1, ingresoId);
        ps.setInt(2, productoId);
        ps.setInt(3, cantidad);
        ps.setDouble(4, precioProducto);
        ps.setDouble(5, costoTotal);

        int result = ps.executeUpdate();
        if (result == 0) {
            System.err.println("Error al insertar un detalle de ingreso en la base de datos.");
            throw new SQLException("Error al insertar un detalle de ingreso en la base de datos.");
        }

        // Actualizar el stock del producto
        actualizarStockProducto(productoId, cantidad);

        // Actualizar el total en la tabla ingresos
        actualizarTotalIngreso(ingresoId, costoTotal);

    } catch (SQLException e) {
        System.err.println("Error al ejecutar la operación: " + e.getMessage());
        throw e;
    }
}


    // Método para obtener el precio del producto
    private double obtenerPrecioProducto(int productoId) throws SQLException {
        String query = "SELECT precio FROM productos WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, productoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio");
                } else {
                    throw new SQLException("No se encontró el producto con ID: " + productoId);
                }
            }
        }
    }

    // Método para actualizar el stock del producto
   private void actualizarStockProducto(int productoId, int cantidad) throws SQLException {
    // Verifica si la cantidad es válida
    if (cantidad == 0) {
        System.err.println("La cantidad para actualizar el stock no puede ser cero.");
        return;
    }

    // Consulta para actualizar el stock
    String query = "UPDATE productos SET stock = stock + ? WHERE id = ?";
    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setInt(1, cantidad);
        ps.setInt(2, productoId);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            System.err.println("No se encontró el producto con id: " + productoId);
        } else {
            System.out.println("Stock actualizado correctamente para el producto con id: " + productoId);
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar el stock del producto: " + e.getMessage());
        throw e;
    }
}

    // Método para actualizar el total en la tabla ingresos
    private void actualizarTotalIngreso(int ingresoId, double costoTotal) throws SQLException {
        String query = "UPDATE ingresos SET total = total + ? WHERE id = ?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setDouble(1, costoTotal);
            ps.setInt(2, ingresoId);
            ps.executeUpdate();
        }
    }
    
      public List<String[]> listar(int ingresoId) throws SQLException {
        List<String[]> detalles = new ArrayList<>();
        String query = "SELECT di.*, p.nombre AS producto_nombre "
                     + "FROM detalle_ingresos di "
                     + "INNER JOIN productos p ON di.producto_id = p.id "
                     + "WHERE di.ingreso_id = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, ingresoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] detalle = {
                        rs.getString("id"),
                        rs.getString("ingreso_id"),
                        rs.getString("producto_nombre"),
                        rs.getString("cantidad"),
                        rs.getString("costo_unitario"),
                        rs.getString("costo_total")
                    };
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de detalles de ingreso: " + e.getMessage());
            throw e;
        }

        return detalles;
    }
    
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }

}
