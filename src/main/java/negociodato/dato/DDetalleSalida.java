package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DDetalleSalida {

    public static final String[] HEADERS
            = {"ID", "FECHA_ORDEN", "TOTAL_ORDEN", "CLIENTE_ID", "ESTADOVENTA_ID"};

    private final DBConeccion connection;

    public DDetalleSalida() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    public void guardar(
            int ordenId,
            int productoId,
            int cantidad) throws SQLException, ParseException {
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

    public List<String[]> listar() throws SQLException {
        List<String[]> ordenes = new ArrayList<>();
        String query = "SELECT * FROM detalle_ordens";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] orden = {
                        rs.getString("id"),
                        rs.getString("fecha_orden"),
                        rs.getString("total_orden"),
                        rs.getString("cliente_id"),
                        rs.getString("estadoventa_id"),
                        rs.getString("updated_at")
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

    public String[] ver(int id) throws SQLException {
        String[] orden = null;
        String query = "SELECT * FROM ordens WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    orden = new String[]{
                        rs.getString("id"),
                        rs.getString("fecha_orden"),
                        rs.getString("total_orden"),
                        rs.getString("cliente_id"),
                        rs.getString("estadoventa_id"),
                        rs.getString("updated_at")
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la orden: " + e.getMessage());
            throw e;
        }
        return orden;
    }
*/

    private void actualizarOrden(int ordenId, double precioTotal) throws SQLException {
        String query = "UPDATE ordens SET total_orden = total_orden + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

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

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}

