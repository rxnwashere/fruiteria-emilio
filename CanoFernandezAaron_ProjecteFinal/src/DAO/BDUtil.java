/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.Connection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aaroncanofdez
 */
public class BDUtil {

    public static void createEstructuraMysql() throws SQLException {
        PreparedStatement stmt;
        
        String drop1 = "DROP TABLE IF EXISTS Producte;";
        
        String drop2 = "DROP TABLE IF EXISTS Categoria;";
        
        String create1 = "CREATE TABLE IF NOT EXISTS Categoria ("
                + "id int auto_increment not null,"
                + "nom varchar(50),"
                + "descripcio varchar(200),"
                + "UNIQUE KEY (id));";

        String create2 = "CREATE TABLE IF NOT EXISTS Producte ("
                + "codi int auto_increment not null,"
                + "nom varchar(50),"
                + "categoria_id int,"
                + "preu decimal(5,2) not null,"
                + "tipusPreu varchar(100),"
                + "stock int not null,"
                + "oferta boolean,"
                + "UNIQUE KEY (codi),"
                + "FOREIGN KEY (categoria_id) REFERENCES Categoria(id));";
        
        stmt = conn.prepareStatement(drop1);
        stmt.executeUpdate();
        
        stmt = conn.prepareStatement(drop2);
        stmt.executeUpdate();

        stmt = conn.prepareStatement(create1);
        stmt.executeUpdate();

        stmt = conn.prepareStatement(create2);
        stmt.executeUpdate();

        stmt.close();
    }

    public static void netejaTaules() {
        PreparedStatement stmt;
        try {
            String query1 = "delete from Producte";
            String query2 = "delete from Categoria";
            stmt = conn.prepareStatement(query1);
            stmt.executeUpdate();
            stmt = conn.prepareStatement(query2);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean codiProducteExisteix(int codi) throws SQLException {
        String query = "SELECT codi FROM Producte WHERE codi = ?;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, codi);
        ResultSet rs = stmt.executeQuery();
        boolean existeix = rs.next();
        rs.close();
        stmt.close();
        return existeix;
    }

    public static boolean idCategoriaExisteix(int id) throws SQLException {
        String query = "SELECT id FROM Categoria WHERE id = ?;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        boolean existeix = rs.next();
        rs.close();
        stmt.close();
        return existeix;
    }
    
    public static boolean isNegatiu(int num) {
        return num < 0;
    }

    public static boolean is0(int num) {
        return num == 0;
    }

    public static boolean isPositive(int num) {
        return num > 0;
    }
}
