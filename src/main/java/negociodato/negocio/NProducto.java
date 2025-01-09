package negociodato.negocio;

import negociodato.dato.DProducto;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.*;


public class NProducto {

    private final DProducto dProducto;
    private final DCategoria dCategoria;

    public NProducto() {
        this.dProducto = new DProducto();
        this.dCategoria = new DCategoria();
    }

    public void guardar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("Parametros vacios!");
        }
        dProducto.guardar(
            parametros.get(0), // cod
            parametros.get(1), // nombre
            parametros.get(2), // descripcion
            parametros.get(3), // unidad
            Double.parseDouble(parametros.get(4)), // precio_venta
            parametros.get(5)  // categoria_id
        );

        dProducto.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException, ParseException {
    if (parametros.isEmpty()) {
        throw new SQLException("¡Parámetros vacíos!");
    }

    int producto_id = dProducto.getIdByCodigo(parametros.get(0));
    if (producto_id != -1) {
        System.out.println("ENTRE A NPRODUCTO.JAVA");
        dProducto.modificar(
            parametros.get(0), // cod
            parametros.get(1), // nombre
            parametros.get(2), // descripcion
            parametros.get(3), // unidad
            Double.parseDouble(parametros.get(4)), // precio
            parametros.get(5)  // categoria
        );
    } else {
        throw new SQLException("El producto con el código especificado no existe.");
    }

    dProducto.desconectar();
}


    public void eliminar(List<String> parametros) throws SQLException {
        int producto_id = dProducto.getIdByCodigo(parametros.get(0));
        if (producto_id != -1) {
            dProducto.eliminar(parametros.get(0));
            dProducto.desconectar();
        }
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> productos = (ArrayList<String[]>) dProducto.listar();
        dProducto.desconectar();
        return productos;
    }
   
  public  ArrayList<String[]> ver(List<String> parametros) throws SQLException {
       int producto_id = dProducto.getIdByNombre(parametros.get(0));
          ArrayList<String[]> productos = (ArrayList<String[]>) dProducto.ver(producto_id);
        dProducto.desconectar();
        return productos;
    }
   /* public ArrayList<String[]> graficaDeProductoMasVendido() throws SQLException {
        ArrayList<String[]> productos = (ArrayList<String[]>) dProducto.graficaDeProductoMasVendido();
        dProducto.desconectar();
        return productos;
    }*/

}
