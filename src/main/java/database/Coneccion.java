/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alison
 */
public class Coneccion {
     public static void main(String[] args) {
        try {
            DBConeccion sqlConnection =
                  
                    new DBConeccion("grupo18sa", "grup018grup018*", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   

            String query = "SELECT * FROM users";
            PreparedStatement ps = sqlConnection.connect().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            System.out.println("resultado: " + rs.next());
        } catch (SQLException ex) {
            Logger.getLogger(Coneccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   /* public void executeQuery() {
      try {
            DBConeccion sqlConnection =
                  
                    new DBConeccion("grupo18sa", "grup018grup018", "mail.tecnoweb.org.bo", "5432", "db_grupo18sa");   

            String query = "SELECT * FROM users";
            PreparedStatement ps = sqlConnection.connect().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                // Supongamos que la tabla 'users' tiene columnas 'id', 'name', y 'email'
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Coneccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
