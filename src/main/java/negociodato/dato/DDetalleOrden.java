package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DDetalleOrden {

    public static final String[] HEADERS
            = {"ID", "ID_OREN", "ID_PRODUCTO", "CANTIFDAD", "PRECIO","TOTAL"};

    private final DBConeccion connection;

    public DDetalleOrden() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    public void guardar(
            int ordenId,
            String nombreProducto,
            int cantidad) throws SQLException, ParseException {
        
         DProducto dProducto = new DProducto();
        int productoId = dProducto.getIdByNombre(nombreProducto);



    
        double precioUnitario = this.getPrecioProducto(productoId);
        double precioTotal = cantidad * precioUnitario;

        String query = "INSERT INTO detalle_ordens(orden_id, producto_id, cantidad, precio_unitario, precio_total,created_at, updated_at)"
                + " VALUES (?, ?, ?, ?, ?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, ordenId);
            ps.setInt(2, productoId);
            ps.setInt(3, cantidad);
            ps.setDouble(4, precioUnitario);
            ps.setDouble(5, precioTotal);

            int result = ps.executeUpdate();
            if (result == 0) {
                System.err.println("Error al insertar un detalle de orden en la base de datos.");
                throw new SQLException("Error al insertar un detalle de orden en la base de datos.");
            }

            actualizarOrden(ordenId, precioTotal);

        } catch (SQLException e) {
            System.err.println("Error al ejecutar la operación: " + e.getMessage());
            throw e;
        }
    }
   /* 
     public void modificar(
            int id,
            double totalOrden,
            int clienteId,
            int estadoVentaId) throws SQLException, ParseException {

        String query = "UPDATE detalle_ordens SET total_orden=?, "
                + "cliente_id=?, estadoventa_id=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setDouble(1, totalOrden);
            ps.setInt(2, clienteId);
            ps.setInt(3, estadoVentaId);
            ps.setInt(4, id);

            int rowsAffected = ps.executeUpdate();

            System.out.println("rowsAffected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.err.println("Class DOrden.java: Ocurrió un error al modificar una orden en el método modificar()");
                throw new SQLException("No se realizó ninguna modificación en la base de datos.");
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM detalle_ordens WHERE id=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                System.err.println("Class DOrden.java dice: "
                        + "Ocurrió un error al eliminar una orden en el método eliminar()");
                throw new SQLException();
            }
        }
    }
*/
 public List<String[]> listar(int ordenId) throws SQLException {
        List<String[]> detalles = new ArrayList<>();
        String query = "SELECT det_orden.*, p.nombre AS producto_nombre "
                     + "FROM detalle_ordens det_orden "
                     + "INNER JOIN productos p ON det_orden.producto_id = p.id "
                     + "WHERE det_orden.orden_id = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, ordenId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] detalle = {
                        rs.getString("id"),
                        rs.getString("orden_id"),
                        rs.getString("producto_nombre"),
                        rs.getString("cantidad"),
                        rs.getString("precio_unitario"),
                        rs.getString("precio_total")
                    };
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de detalles de la orden : " + ordenId + " - " + e.getMessage());
            throw e;
        }

        return detalles;
    }

    private void actualizarOrden(int ordenId, double precioTotal) throws SQLException {
        String query = "UPDATE ordens SET total = total + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setDouble(1, precioTotal);
            ps.setInt(2, ordenId);

            int result = ps.executeUpdate();
            if (result == 0) {
                System.err.println("Error al actualizar el total de la orden en la base de datos.");
                throw new SQLException("Error al actualizar el total de la orden en la base de datos.");
            }
        }
    }
public double getPrecioProducto(int id) throws SQLException {
        double precio = 0.0;
        String query = "SELECT p.precio FROM productos p INNER JOIN detalle_ordens d ON p.id = d.producto_id WHERE d.producto_id = ?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            precio = set.getDouble("precio");
        }
        System.out.println("producto precio: " + precio);
        return precio;
    }
public String getNombreProducto(int id) throws SQLException {
        String precio = "";
        String query = "SELECT p.nombre FROM productos p INNER JOIN detalle_ordens d ON p.id = d.producto_id WHERE d.producto_id = ?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            precio = set.getString("nombre");
        }
        System.out.println("producto: " + precio);
        return precio;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}

