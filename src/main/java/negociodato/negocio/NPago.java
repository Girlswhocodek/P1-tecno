package negociodato.negocio;

import negociodato.dato.DProducto;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import negociodato.dato.*;


public class NPago {

    private final DPago dPago;

    public NPago() {
        this.dPago = new DPago();
    }

    /*public void guardar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dPago.guardar(
            Integer.parseInt(parametros.get(0)), // cod
            Integer.parseInt(parametros.get(1)), // nombre
            Integer.parseInt(parametros.get(2)), // descripcion
            parametros.get(3), // unidad
            Integer.parseInt(parametros.get(4)),
            Double.parseDouble(parametros.get(5)) // precio_venta ca
        );

        dPago.desconectar();
    }*/

    public void modificar(List<String> parametros) throws SQLException, ParseException {
        if (parametros.isEmpty()) {
            throw new SQLException("¡Parámetros vacíos!");
        }
        dPago.modificar(
                Integer.parseInt(parametros.get(0)),
                Integer.parseInt(parametros.get(1))
        );

        dPago.desconectar();
    }

    /*public void eliminar(List<String> parametros) throws SQLException {
        int orden_id = Integer.parseInt(parametros.get(0));
        dOrden.eliminar(orden_id);
        dOrden.desconectar();
    }*/

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> ordenes = (ArrayList<String[]>) dPago.listar();
        dPago.desconectar();
        return ordenes;
    }
}

