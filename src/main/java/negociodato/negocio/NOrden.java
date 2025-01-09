package negociodato.negocio;

import negociodato.dato.DProducto;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.*;


public class NOrden {

    private final DOrden dOrden;

    public NOrden() {
        this.dOrden = new DOrden();
    }

    public void guardar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dOrden.guardar(
                Integer.parseInt(parametros.get(0))
        );

        dOrden.desconectar();
    }

    public void modificar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dOrden.modificar(
                Integer.parseInt(parametros.get(0)),
                Integer.parseInt(parametros.get(1))
        );

        dOrden.desconectar();
    }

    /*public void eliminar(List<String> parametros) throws SQLException {
        int orden_id = Integer.parseInt(parametros.get(0));
        dOrden.eliminar(orden_id);
        dOrden.desconectar();
    }*/

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> ordenes = (ArrayList<String[]>) dOrden.listar();
        dOrden.desconectar();
        return ordenes;
    }
}

