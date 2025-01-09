package negociodato.dato;

import database.DBConeccion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DProducto {

    public static final String[] HEADERS
            = {"ID,", "COD_PRO", "NOMBRE", "DESCRIPCION","UNIDAD" , "PRECIO", "STOCK", "CATEGORIA"};

    private final DBConeccion connection;

    public DProducto() {
         connection = new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   
    }

    public void guardar(
            String cod,
            String nombre,
            String descripcion,
            String unidad,
            double precio,
            String categoria) throws SQLException, ParseException {
        
            int idcategoria = this.getIdOfCategoria(categoria);

        String query = "INSERT INTO productos(cod_barra, nombre, descripcion,unidad, imagen , precio,stock,stock_min , categoria_id, created_at, updated_at)"
                + " VALUES (?, ?, ?, ? , ? , ? , ? ,? , ? , CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        String descripcion_capitalizado = descripcion.substring(0, 1).toUpperCase() + descripcion.substring(1).toLowerCase();

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setString(1, cod.toUpperCase());
            ps.setString(2, nombre.toUpperCase());
            ps.setString(3,descripcion_capitalizado);
            ps.setString(4,unidad.toUpperCase());
            ps.setString(5,"SIN IMAGEN");
            ps.setDouble(6, precio);
            ps.setInt(7, 0);
            ps.setDouble(8, 5);
            ps.setInt(9, idcategoria);

            int result = ps.executeUpdate();
            if (result == 0) {
                System.err.println("Error al insertar un producto en la base de datos.");
                throw new SQLException("Error al insertar un producto en la base de datos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la operación: " + e.getMessage());
            throw e;
        }
    }
public void modificar(
        String cod,
        String nombre,
        String descripcion,
        String unidad,
        double precio,
        String categoria) throws SQLException, ParseException {

    int idcategoria = this.getIdOfCategoria(categoria);
    int id = this.getIdByCodigo(cod);

    System.out.println("ID del producto: " + id);  // Verifica que obtuviste el ID correcto

    if (id == -1) {
        throw new SQLException("El producto con el código especificado no existe.");
    }

    System.out.println("********DProducto.modificar()***********");
    System.out.println(nombre + " - " + descripcion + " - "  + unidad + " - " + precio + " - " + categoria + " - " + id);
    
    String query = "UPDATE productos SET nombre=?, descripcion=?, unidad=?, "
            + "precio=?, categoria_id=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

    String descripcion_capitalizado = descripcion.substring(0, 1).toUpperCase() + descripcion.substring(1).toLowerCase();

    try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
        System.out.println("Ejecutando consulta SQL: " + query);  // Depuración

        ps.setString(1, nombre.toUpperCase());
        ps.setString(2, descripcion_capitalizado);
        ps.setString(3, unidad.toUpperCase());
        ps.setDouble(4, precio);
        ps.setInt(5, idcategoria);
        ps.setInt(6, id);

        int rowsAffected = ps.executeUpdate();
        System.out.println("rowsAffected: " + rowsAffected);  // Depuración

        if (rowsAffected == 0) {
            System.err.println("Ocurrió un error al modificar el producto.");
            throw new SQLException("No se realizó ninguna modificación en la base de datos.");
        }
    } catch (SQLException e) {
        System.err.println("Error al ejecutar la operación: " + e.getMessage());
        e.printStackTrace();  // Mostrar traza completa
        throw e;
    }
}


   public void eliminar(String cod) throws SQLException {
    int id = this.getIdByCodigo(cod);
    String query = "DELETE FROM productos WHERE id=?";
    PreparedStatement ps = connection.connect().prepareStatement(query);
    ps.setInt(1, id);
    if (ps.executeUpdate() == 0) {
        System.err.println("Ocurrió un error al eliminar un producto.");
        throw new SQLException("No se pudo eliminar el producto.");
    }
}


    public List<String[]> listar() throws SQLException {
        List<String[]> productos = new ArrayList<>();
        String query = "SELECT c.nombre AS categoria_nombre, p.* FROM categorias c INNER JOIN productos p ON c.id = p.categoria_id";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] producto = {
                        rs.getString("id"),
                        rs.getString("cod_barra"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad"),
                        rs.getString("precio"),
                        rs.getString("stock"),
                        rs.getString("categoria_nombre")
                    };
                    productos.add(producto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de productos: " + e.getMessage());
            throw e;
        }

        return productos;
    }

    public List<String[]> ver(int id) throws SQLException {
       List<String[]> productos = null;
        String query = "SELECT * FROM productos WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                  String[] producto = {
                        rs.getString("id"),
                        rs.getString("cod_barra"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("unidad"),
                        rs.getString("precio"),
                        rs.getString("stock"),
                        rs.getString("categoria_nombre")
                    };
                  productos.add(producto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el producto: " + e.getMessage());
            throw e;
        }
        return productos;
    }

    public int getIdOfCategoria(String categoria) throws SQLException {
        System.out.println("getIdOfCategoria(): " + categoria.toUpperCase());
        int id = -1;
        String query = "SELECT id FROM categorias WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, categoria.toUpperCase());

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            id = set.getInt("id");
        }
        return id;
    }
    public int getfCategoria(int id) throws SQLException {
        int categoria = -1;
        String query = "SELECT c.nombre FROM categorías c INNER JOIN productos p ON c.id = p.categoria_id";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            categoria = set.getInt("categoria");
        }
        System.out.println("producto precio: " + categoria);
        return categoria;
    }

    public int getPreciobyId(int id) throws SQLException {
        int precio = -1;
        String query = "SELECT precioventa FROM productos WHERE id=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setInt(1, id);

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            precio = set.getInt("precio");
        }
        System.out.println("producto precio: " + precio);
        return precio;
    }
    public int getIdByNombre(String nombre) throws SQLException {
        System.out.println("getIdByNombre(): " + nombre.toUpperCase());
        int id = -1;
        String query = "SELECT id FROM productos WHERE nombre=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, nombre.toUpperCase());

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            id = set.getInt("id");
        }
        return id;
    }
    public int getIdByCodigo(String cod) throws SQLException {
    int id = -1;
    String query = "SELECT id FROM productos WHERE cod_barra=?";
    PreparedStatement ps = connection.connect().prepareStatement(query);
    ps.setString(1, cod.toUpperCase());

    ResultSet set = ps.executeQuery();
    if (set.next()) {
        id = set.getInt("id");
    }
    return id;
}


   /* public List<String[]> graficaDeProductoMasVendido() throws SQLException {
        List<String[]> productos = new ArrayList<>();
        String query
                = "SELECT productos.descripcion, "
                + "sum(ventas.monto) as total, "
                + "sum(ventas.cantidad) as cantidad_comprada\n "
                + "FROM ventas JOIN productos ON\n"
                + "ventas.producto_id = productos.id "
                + "GROUP BY productos.id, ventas.nro_venta "
                + "ORDER BY ventas.nro_venta";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ResultSet set = ps.executeQuery();
        while (set.next()) {
            productos.add(new String[]{
                //                String.valueOf(set.getInt("id")),
                set.getString("descripcion"),
                String.valueOf(set.getInt("total")),
                String.valueOf(set.getInt("cantidad_comprada")),});
        }
        return productos;
    }
*/
   /* public void modificarCantidad(int id, int stock) throws SQLException, ParseException {
        String query = "UPDATE productos SET stock=?, updated_at=CURRENT_TIMESTAMP WHERE id=?";

        try (PreparedStatement ps = connection.connect().prepareStatement(query)) {
            ps.setInt(1, stock - 1);
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();

            System.out.println("rowsAffected: " + rowsAffected);
            if (rowsAffected == 0) {
                System.err.println("Class DProducto.java: Ocurrió un error al modificar un producto en el método modificar()");
                throw new SQLException("No se realizó ninguna modificación en la base de datos.");
            }
        }
    }
*/
 /*   public int getStockbyCodigo(String codigo) throws SQLException {
        int stock = -1;
        String query = "SELECT stock FROM productos WHERE barracodigo=?";
        PreparedStatement ps = connection.connect().prepareStatement(query);
        ps.setString(1, codigo.toUpperCase());

        ResultSet set = ps.executeQuery();
        if (set.next()) {
            stock = set.getInt("stock");
        }
        return stock;
    }
*/
    public void desconectar() {
        if (connection != null) {
            connection.closeConnection();
        }
    }
}
