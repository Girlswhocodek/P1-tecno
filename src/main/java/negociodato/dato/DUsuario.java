/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negociodato.dato;

import database.DBConeccion;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DUsuario {
    public static final String[] HEADERS = { "Id","name", "email", "rol" };

    private final DBConeccion connection;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 9;

    // Inicializar conexión
    public DUsuario() {
        connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");
    }

    // Generar token de autenticación
    public static String generateToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            token.append(randomChar);
        }

        return token.toString();
    }

    // Guardar un nuevo usuario junto con su rol
    public void guardar(String name, String email, String password, String rol) throws SQLException {
        String query = "INSERT INTO users (name, email, password, created_at, updated_at) "
                + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, name.toUpperCase());
            ps.setString(2, email);
            ps.setString(3, password);

            int result = ps.executeUpdate();

            if (result == 0) {
                throw new SQLException("Error al insertar usuario");
            }

            // Asignar el rol al usuario recién creado
            asignarRol(email, rol);

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage()+ "recuerde que solo se aceptan numeros en el campo password");
            throw e;
        }
    }

    // Asignar un rol a un usuario
    public void asignarRol(String email, String rol) throws SQLException {
        int userId = getIdByEmail(email);
        if (userId == -1) {
            throw new SQLException("Usuario no encontrado para asignar rol");
        }

        String query = "INSERT INTO model_has_roles (role_id, model_type, model_id) "
                + "SELECT roles.id, 'App\\Models\\User', ? FROM roles WHERE roles.name = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, rol);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error al asignar rol al usuario");
            }
        }
    }

    // Modificar usuario y su rol
    public void modificar(long id, String name, String email, String password, String rol) throws SQLException {
    String query = "UPDATE users SET name=?, email=?, password=? WHERE id=?";

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        ps.setString(1, name.toUpperCase());
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setLong(4, id);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Error al modificar usuario: No se encontraron filas para actualizar");
        }

        // Modificar el rol asignado
        modificarRol(id, rol);

    } catch (SQLException e) {
        System.err.println("Error al modificar usuario: " + e.getMessage());
        throw e;
    }
}

    // Modificar el rol de un usuario
    public void modificarRol(long userId, String nuevoRol) throws SQLException {
        // Primero eliminamos el rol actual del usuario
        eliminarRol(userId);

        // Asignar el nuevo rol
        String query = "INSERT INTO model_has_roles (role_id, model_type, model_id) "
                + "SELECT roles.id, 'App\\Models\\User', ? FROM roles WHERE roles.name = ?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setLong(1, userId);
            ps.setString(2, nuevoRol);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error al asignar el nuevo rol al usuario");
            }
        }
    }

    // Eliminar un rol de un usuario
    public void eliminarRol(long userId) throws SQLException {
        String query = "DELETE FROM model_has_roles WHERE model_id=? AND model_type='App\\Models\\User'";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setLong(1, userId);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error al eliminar rol del usuario");
            }
        }
    }

    // Eliminar un usuario y su rol
    public void eliminar(int id) throws SQLException {
        // Primero eliminamos el rol del usuario
        eliminarRol(id);

        // Luego eliminamos el usuario
        String query = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Error al eliminar usuario");
            }
        }
    }

    // Listar usuarios
    public List<String[]> listar() throws SQLException {
        List<String[]> usuarios = new ArrayList<>();
        String query = "SELECT users.id, users.name, users.email, roles.name AS role "
                + "FROM users "
                + "LEFT JOIN model_has_roles ON users.id = model_has_roles.model_id "
                + "LEFT JOIN roles ON model_has_roles.role_id = roles.id";

        try (PreparedStatement ps = connection.connect().prepareStatement(query);
             ResultSet set = ps.executeQuery()) {

            while (set.next()) {
                usuarios.add(new String[]{
                        String.valueOf(set.getInt("id")),
                        set.getString("name"),
                        set.getString("email"),
                        set.getString("role")
                });
            }
        }
        return usuarios;
    }

    // Obtener usuario por ID
    public String[] ver(int id) throws SQLException {
        String[] usuario = null;
        String query = "SELECT users.id, users.name, users.email, roles.name AS role "
                + "FROM users "
                + "LEFT JOIN model_has_roles ON users.id = model_has_roles.model_id "
                + "LEFT JOIN roles ON model_has_roles.role_id = roles.id "
                + "WHERE users.id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    usuario = new String[]{
                            String.valueOf(set.getInt("id")),
                            set.getString("name"),
                            set.getString("email"),
                            set.getString("role")
                    };
                }
            }
        }
        return usuario;
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
            throw new SQLException("Error al obtener ID por email", e);
        }

        return id;
    }

    // Obtener email de usuario por ID
    public String getEmailById(int id) throws SQLException {
        String email = "";
        String query = "SELECT email FROM users WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setLong(1, id);

            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    email = set.getString("email");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener email por id", e);
        }

        return email;
    }

    // Desconectar la base de datos
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
