    package negociodato.negocio;

import negociodato.dato.DProducto;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.*;


public class NDetalleOrden {

     private final DDetalleOrden dDetalleOrden;

    public NDetalleOrden() {
        this.dDetalleOrden = new DDetalleOrden();
    }

     public void guardar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dDetalleOrden.guardar(
                Integer.parseInt(parametros.get(0)),
                 parametros.get(1),
                Integer.parseInt(parametros.get(2))
        );

        dDetalleOrden.desconectar();
    }

   /* public void modificar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dDetalleOrden.modificar(
                Integer.parseInt(parametros.get(0)),
                Double.parseDouble(parametros.get(1)),
                Integer.parseInt(parametros.get(2)),
                Integer.parseInt(parametros.get(3))
        );

        dDetalleOrden.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        int orden_id = Integer.parseInt(parametros.get(0));
        dDetalleOrden.eliminar(orden_id);
        dDetalleOrden.desconectar();
    }
*/
    public ArrayList<String[]> listar(List<String> parametros) throws SQLException {
        
        ArrayList<String[]> detalles = (ArrayList<String[]>) dDetalleOrden.listar(Integer.parseInt(parametros.get(0)));
        dDetalleOrden.desconectar();
        return detalles;
    }

}

