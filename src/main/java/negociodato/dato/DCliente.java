package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DCliente {
    public static final String[] HEADERS
            = {"ID", "NOMBRE", "DIRECCION", "EMAIL", "TELEFONO", "SEXO", "CEDULA", "ROL"};

    private final DBConeccion connection;

    public DCliente() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   
    }

    // Guardar un cliente
    public void guardar(
            String nombre,
            String email,
            String password,
            String direccion,
            String telefono,
            String sexo,
            String cedula_nit) throws SQLException, ParseException {

        // Insertar o actualizar el usuario con el rol cliente
        String query = "INSERT INTO users (name, email, password, direccion, telefono, sexo, ci_nit, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) "
                + "ON CONFLICT (email) DO UPDATE SET name=excluded.name, direccion=excluded.direccion, "
                + "telefono=excluded.telefono, sexo=excluded.sexo, ci_nit=excluded.ci_nit, updated_at=CURRENT_TIMESTAMP";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, nombre.toUpperCase());
            ps.setString(2, email);
            ps.setString(3, password); // Aquí puedes manejar el password según tu lógica
            ps.setString(4, direccion);
            ps.setString(5, telefono);
            ps.setString(6, sexo);
            ps.setString(7, cedula_nit);

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new SQLException("Error al insertar o actualizar usuario");
            }

            // Asignar el rol de cliente
            asignarRol(email, "cliente");
        } catch (SQLException e) {
            System.err.println("Error al guardar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Modificar datos del cliente
    public void modificar(
            int id,
            String nombre,
            String direccion,
            String telefono,
            String email,
            String cedula_nit,
            String sexo
    ) throws SQLException, ParseException {

        // Modificar datos del usuario
        String query = "UPDATE users SET name=?, direccion=?, telefono=?, email=?, ci_nit=?, sexo=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";
        
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, nombre.toUpperCase());
            ps.setString(2, direccion);
            ps.setString(3, telefono);
            ps.setString(4, email);
            ps.setString(5, cedula_nit);
            ps.setString(6, sexo);
            ps.setInt(7, id);

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new SQLException("Error al modificar usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Eliminar cliente (en realidad eliminar el rol)
    public void eliminar(int id) throws SQLException {
        // Primero eliminar el rol del usuario
        eliminarRol(id);

        // Luego eliminar el usuario
        String query = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new SQLException("Error al eliminar usuario");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            throw e;
        }
    }

    // Asignar rol al usuario
    private void asignarRol(String email, String rol) throws SQLException {
        int userId = getIdByEmail(email);
        if (userId == -1) {
            throw new SQLException("Usuario no encontrado para asignar rol");
        }

        String query = "INSERT INTO model_has_roles (role_id, model_type, model_id) "
                + "SELECT roles.id, 'App\\Models\\User', ? FROM roles WHERE roles.name = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, rol);

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new SQLException("Error al asignar rol al usuario");
            }
        }
    }

    // Eliminar rol del usuario
    private void eliminarRol(long userId) throws SQLException {
        String query = "DELETE FROM model_has_roles WHERE model_id=? AND model_type='App\\Models\\User'";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setLong(1, userId);

            int result = ps.executeUpdate();
            if (result == 0) {
                throw new SQLException("Error al eliminar rol del usuario");
            }
        }
    }

    // Listar clientes (usuarios con rol cliente)
    public List<String[]> listar() throws SQLException {
        List<String[]> clientes = new ArrayList<>();
        String query = "SELECT users.id, users.name, users.direccion, users.email, users.telefono, users.sexo, users.ci_nit, roles.name AS role "
                + "FROM users "
                + "LEFT JOIN model_has_roles ON users.id = model_has_roles.model_id "
                + "LEFT JOIN roles ON model_has_roles.role_id = roles.id "
                + "WHERE roles.name = 'cliente'";

        try (PreparedStatement ps = connection.connect().prepareStatement(query);
             ResultSet set = ps.executeQuery()) {

            while (set.next()) {
                clientes.add(new String[]{
                        String.valueOf(set.getInt("id")),
                        set.getString("name"),
                        set.getString("direccion"),
                        set.getString("email"),
                        set.getString("telefono"),
                        set.getString("sexo"),
                        set.getString("ci_nit"),
                        set.getString("role")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de clientes: " + e.getMessage());
            throw e;
        }
        return clientes;
    }

    // Ver datos del cliente por ID
    public List<String[]> ver(int id) throws SQLException {
        List<String[]> clientes = new ArrayList<>();
        String query = "SELECT users.id, users.name, users.direccion, users.email, users.telefono, users.sexo, users.ci_nit, roles.name AS role "
                + "FROM users "
                + "LEFT JOIN model_has_roles ON users.id = model_has_roles.model_id "
                + "LEFT JOIN roles ON model_has_roles.role_id = roles.id "
                + "WHERE users.id=? AND roles.name = 'cliente'";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    clientes.add(new String[]{
                            String.valueOf(set.getInt("id")),
                            set.getString("name"),
                            set.getString("direccion"),
                            set.getString("email"),
                            set.getString("telefono"),
                            set.getString("sexo"),
                            set.getString("ci_nit")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos del cliente: " + e.getMessage());
            throw e;
        }
        return clientes;
    }

    // Obtener ID de usuario por email
    public int getIdByEmail(String email) throws SQLException {
        int id = -1;
        String query = "SELECT id FROM users WHERE email=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, email);

            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    id = set.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID por email: " + e.getMessage());
            throw e;
        }
        return id;
    }

    // Desconectar la base de datos
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
