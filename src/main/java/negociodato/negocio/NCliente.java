package negociodato.negocio;

import negociodato.dato.DCliente;
import negociodato.dato.DUsuario;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NCliente {

    private final DUsuario dUsuario;
    private final DCliente dCliente;

    public NCliente() {
        this.dUsuario = new DUsuario();
        this.dCliente = new DCliente();
    }

    public int guardar(List<String> parametros) throws SQLException {
        // Parámetros deben ser:
        // 0: nombre
        // 1: direccion
        // 2: telefono
        // 3: email
        // 4: ci_nit
        // 5: sexo

        // Verifica si el email ya está registrado
        int idUsuario = dUsuario.getIdByEmail(parametros.get(3));
        if (idUsuario == -1) {
            try {
                int idCliente =dCliente.guardar(
                        parametros.get(0), //name
                        parametros.get(1),  // email
                        parametros.get(2),  // direccion
                        parametros.get(3),  // telefono
                        parametros.get(4),  // sexo
                        parametros.get(5)   // ci_nit 
                );
                System.out.println("Cliente guardado correctamente.");
                dCliente.desconectar();
                return idCliente;
            } catch (ParseException ex) {
                Logger.getLogger(NCliente.class.getName()).log(Level.SEVERE, null, ex);
                throw new SQLException("Error al guardar el cliente", ex);
            }
        } else {
            System.out.println("El email ya fue registrado.");
        }

        dCliente.desconectar();
        return -1;
    }

    public void modificar(List<String> parametros) throws SQLException, ParseException {
        // Encuentra el usuario por email
        int idUsuario = dUsuario.getIdByEmail(parametros.get(3));

        // Verifica si el email existe
        if (idUsuario != -1) {
            // Actualiza el usuario y el cliente
            dCliente.modificar(
                    idUsuario,
                    parametros.get(0),  // nombre
                    parametros.get(1),  // direccion
                    parametros.get(2),  // telefono
                    parametros.get(3),  // email
                    parametros.get(4),  // ci_nit
                    parametros.get(5)   // sexo
            );
        }

        dCliente.desconectar();
    }

    public void eliminar(List<String> parametros) throws SQLException {
        dCliente.eliminar(Integer.parseInt(parametros.get(0)));
        /*int user_id = dUsuario.getIdByEmail(parametros.get(0));*/
        /*if (user_id != -1) {
            dCliente.eliminar(user_id);
            dUsuario.eliminar(user_id);
        } else {
            System.out.println("El usuario con el email proporcionado no existe.");
        }

        dUsuario.desconectar();*/
        dCliente.desconectar();
    }

    public ArrayList<String[]> listar() throws SQLException {
        ArrayList<String[]> clientes = (ArrayList<String[]>) dCliente.listar();
        dCliente.desconectar();
        return clientes;
    }

    public String[] ver(int id) throws SQLException {
        //int user_id = dUsuario.getIdByEmail(parametros.get(0));
        String[] clientes = (String[]) dCliente.ver(id);
        dCliente.desconectar();
        return clientes;
    }
}
