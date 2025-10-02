/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import static DAO.Connection.conn;
import Model.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aaroncanofdez
 */
public class CategoriaDAO extends Connection {

    private static CategoriaDAO instancia = null;

    private CategoriaDAO() {
        this.connect();
    }

    public static CategoriaDAO getInstance() {
        if (instancia == null) {
            instancia = new CategoriaDAO();
        }
        return instancia;
    }

    public boolean afegirCategoria(Categoria c) throws SQLException {
        PreparedStatement stmt;
        String insert = "INSERT INTO Categoria (nom, descripcio) VALUES (?, ?);";
        stmt = conn.prepareStatement(insert);
        stmt.setString(1, c.getNom());
        stmt.setString(2, c.getDescripcio());

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public boolean modificarCategoria(Categoria c) throws SQLException {
        PreparedStatement stmt;
        String update = "UPDATE Categoria set nom = ?, descripcio = ? WHERE id = ?;";
        stmt = conn.prepareStatement(update);
        stmt.setString(1, c.getNom());
        stmt.setString(2, c.getDescripcio());
        stmt.setInt(3, c.getId());

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public boolean eliminarCategoria(int id) throws SQLException {
        String query = "DELETE FROM Categoria WHERE id = ?;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public ArrayList<Categoria> mostrarCategories() throws SQLException {
        ArrayList<Categoria> cats = new ArrayList<>();
        String select = "SELECT * FROM Categoria ORDER BY id;";
        PreparedStatement stmt = conn.prepareStatement(select);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Categoria c = new Categoria();

            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setDescripcio(rs.getString("descripcio"));

            cats.add(c);
        }

        return cats;
    }

    public ArrayList<Categoria> mostrarCategories(String campOrdre) throws SQLException {
        ArrayList<Categoria> cats = new ArrayList<>();
        campOrdre = campOrdre.toLowerCase();
        String select = "SELECT * FROM Categoria ORDER BY " + campOrdre + ";";
        PreparedStatement stmt = conn.prepareStatement(select);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Categoria c = new Categoria();

            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setDescripcio(rs.getString("descripcio"));

            cats.add(c);
        }

        return cats;
    }

    public Categoria obtenirCategoriaPerId(int id) throws SQLException {
        String select = "SELECT * FROM Categoria WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        Categoria c = new Categoria();

        while (rs.next()) {
            c.setId(rs.getInt("id"));
            c.setNom(rs.getString("nom"));
            c.setDescripcio(rs.getString("descripcio"));
        }

        return c;
    }

    public int returnId(String nom) throws SQLException {
        String select = "SELECT id FROM Categoria WHERE nom = ? LIMIT 1;";
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setString(1, nom);
        ResultSet rs = stmt.executeQuery();
        int id = 0;

        while (rs.next()) {
            id = rs.getInt("id");
        }
        
        return id;
    }
    
    public boolean isCatAssignada(int id) throws SQLException {
        String select = "SELECT count(categoria_id) FROM Producte WHERE categoria_id = ?;";
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        int count = 0;
        
        while (rs.next()) {            
            count = rs.getInt(1);
        }
        
        rs.close();
        stmt.close();
        
        return count >= 1;
    }
}
