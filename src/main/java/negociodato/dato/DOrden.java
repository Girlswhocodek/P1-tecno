package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DOrden {

    public static final String[] HEADERS
            = {"ID", "FECHA_ORDEN", "TOTAL_ORDEN", "CLIENTE_ID", "ESTADOVENTA_ID"};

    private final DBConeccion connection;

    public DOrden() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    public void guardar(
            int idCliente ) throws SQLException, ParseException {

        String query = "INSERT INTO ordens(fecha, total, cliente_id, estado_id,created_at, updated_at)"
                + " VALUES (CURRENT_TIMESTAMP, 0, ?, 9, CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

     
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, idCliente);

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

    public void modificar(
            int id,
            int estadoVentaId) throws SQLException, ParseException {

        String query = "UPDATE ordens SET "
                + " estado_id=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, estadoVentaId);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();

            System.out.println("rowsAffected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.err.println("Class DOrden.java: Ocurrió un error al modificar una orden en el método modificar()");
                throw new SQLException("No se realizó ninguna modificación en la base de datos.");
            }
        }
    }

    /*public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM ordens WHERE id=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                System.err.println("Class DOrden.java dice: "
                        + "Ocurrió un error al eliminar una orden en el método eliminar()");
                throw new SQLException();
            }
        }
    }*/

    public List<String[]> listar() throws SQLException {
        List<String[]> ordenes = new ArrayList<>();
        String query = "SELECT * FROM ordens";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] orden = {
                        rs.getString("id"),
                        rs.getString("fecha"),
                        rs.getString("total"),
                        rs.getString("cliente_id"),
                        rs.getString("estado_id"),
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

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}

