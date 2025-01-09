package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import negociodato.utils.DateString;

public class DSalida {

    public static final String[] HEADERS
            = {"ID", "ORDEN", "ESTADO_SALIDA", "FECHA_SALIDA", "MOTIVO"};

    private final DBConeccion connection;

    public DSalida() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    public void guardar(
            int ordenId,
            int estadoId,
            String fecha,
            String motivo) throws SQLException, ParseException {

        String query = "INSERT INTO salidas(orden_id, estadosalida_id, fecha_salida, motivo, updated_at)"
                + " VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
           
            ps.setInt(1, ordenId);
            ps.setInt(2, estadoId);
            ps.setDate(2, DateString.StringToDateSQL(fecha));
            ps.setString(4,motivo);

            int result = ps.executeUpdate();
            if (result == 0) {
                System.err.println("Error al insertar una orden en la base de datos.");
                throw new SQLException("Error al insertar una orden en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la operación: " + e.getMessage());
            throw e;
        }
    }

    public void modificar(
            int id,
            int ordenId,
            int estadoId,
            String fecha,
            String motivo) throws SQLException, ParseException {

        String query = "UPDATE salidas SET estadosalida_=?, "
                + "fecha_salida=?, motivo=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
             ps.setInt(1, ordenId);
            ps.setInt(2, estadoId);
            ps.setDate(2, DateString.StringToDateSQL(fecha));
            ps.setString(4,motivo);
            ps.setInt(5, id);

            int rowsAffected = ps.executeUpdate();

            System.out.println("rowsAffected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.err.println("Class DOrden.java: Ocurrió un error al modificar una orden en el método modificar()");
                throw new SQLException("No se realizó ninguna modificación en la base de datos.");
            }
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM salidas WHERE id=?";
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
        List<String[]> salidas = new ArrayList<>();
        String query = "SELECT * FROM salidas";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] salida = {
                        rs.getString("orden_id"),
                        rs.getString("estadosalida_id"),
                        rs.getString("fecha_salida"),
                        rs.getString("motivo"),
                    };
                    salidas.add(salida);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de órdenes: " + e.getMessage());
            throw e;
        }

        return salidas;
    }

    public String[] ver(int id) throws SQLException {
        String[] salida = null;
        String query = "SELECT * FROM salidas WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    salida = new String[]{
                        rs.getString("orden_id"),
                        rs.getString("estadosalida_id"),
                        rs.getString("fecha_salida"),
                        rs.getString("motivo"),
                    };
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la orden: " + e.getMessage());
            throw e;
        }
        return salida;
    }

    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}

