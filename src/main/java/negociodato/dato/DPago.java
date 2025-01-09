package negociodato.dato;

import database.DBConeccion;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DPago {

    public static final String[] HEADERS
            = {"ID","ORDEN_ID","METODO_PAGO_ID","ESTDAO_ID","NOMBRE", "TRANSACCION", "MONTO_PAGO","FECHA_PAGO"};

    private final DBConeccion connection;

    public DPago() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    public void guardar(
        int ordenId, int metodoPagoId, int estadoId, String nombre, String transaccion, double montoPago) throws SQLException {

    String query = "INSERT INTO pagos(orden_id, metodo_pago_id, estado_id, nombre, transaccion, monto_pago, fecha_pago, created_at, updated_at)"
            + " VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setInt(1, ordenId);
        ps.setInt(2, metodoPagoId);
        ps.setInt(3, estadoId);
        ps.setString(4, nombre);
        ps.setString(5, transaccion);
        ps.setDouble(6, montoPago);

        int result = ps.executeUpdate();
        if (result == 0) {
            System.err.println("Error al insertar un pago en la base de datos.");
            throw new SQLException("Error al insertar un pago en la base de datos.");
        }
    } catch (SQLException e) {
        System.err.println("Error al ejecutar la operación: " + e.getMessage());
        throw e;
    }
}


 public void modificar(
        int id, int estadoId) throws SQLException {

    String query = "UPDATE pagos SET estado_id=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setInt(1, estadoId);
        ps.setInt(2, id);

        int rowsAffected = ps.executeUpdate();

        System.out.println("rowsAffected: " + rowsAffected);
        if (rowsAffected == 0) {
            System.err.println("Class DPago.java: Ocurrió un error al modificar un pago en el método modificar()");
            throw new SQLException("No se realizó ninguna modificación en la base de datos.");
        }
    }
}



    public List<String[]> listar() throws SQLException {
    List<String[]> pagos = new ArrayList<>();
    String query = "SELECT * FROM pagos";

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String[] pago = {
                    rs.getString("id"),
                    rs.getString("orden_id"),
                    rs.getString("metodo_pago_id"),
                    rs.getString("estado_id"),
                    rs.getString("nombre"),
                    rs.getString("transaccion"),
                    rs.getString("monto_pago"),
                    rs.getString("fecha_pago"),
                };
                pagos.add(pago);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener la lista de pagos: " + e.getMessage());
        throw e;
    }

    return pagos;
}


    public String[] ver(int id) throws SQLException {
        String[] orden = null;
        String query = "SELECT * FROM pagos WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    orden = new String[]{
                    rs.getString("id"),
                    rs.getString("orden_id"),
                    rs.getString("metodo_pago_id"),
                    rs.getString("estado_id"),
                    rs.getString("nombre"),
                    rs.getString("transaccion"),
                    rs.getString("monto_pago"),
                    rs.getString("fecha_pago"),
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

