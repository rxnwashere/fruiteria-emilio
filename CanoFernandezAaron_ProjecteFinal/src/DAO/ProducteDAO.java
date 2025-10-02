package DAO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Model.Producte;
import Model.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author aaroncanofdez
 */
public class ProducteDAO extends Connection {

    private static ProducteDAO instancia = null;

    private ProducteDAO() {
        this.connect();
    }

    public static ProducteDAO getInstance() {
        if (instancia == null) {
            instancia = new ProducteDAO();
        }
        return instancia;
    }

    public boolean afegirProducte(Producte p) throws SQLException {
        PreparedStatement stmt;
        String insert = "INSERT INTO Producte (nom, categoria_id, preu, tipusPreu, stock, oferta) VALUES (?, ?, ?, ?, ?, ?);";
        stmt = conn.prepareStatement(insert);
        stmt.setString(1, p.getNom());
        stmt.setInt(2, p.getCategoria().getId());
        stmt.setDouble(3, p.getPreu());
        stmt.setString(4, p.getTipusPreu());
        stmt.setInt(5, p.getStock());
        stmt.setBoolean(6, p.isOferta());

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public boolean modificarProducte(Producte p) throws SQLException {
        PreparedStatement stmt;
        String update = "UPDATE Producte set nom = ?, categoria_id = ?, preu = ?, tipusPreu = ?, stock = ?, oferta = ? WHERE codi = ?;";
        stmt = conn.prepareStatement(update);
        stmt.setString(1, p.getNom());
        stmt.setInt(2, p.getCategoria().getId());
        stmt.setDouble(3, p.getPreu());
        stmt.setString(4, p.getTipusPreu());
        stmt.setInt(5, p.getStock());
        stmt.setBoolean(6, p.isOferta());
        stmt.setInt(7, p.getCodi());

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public boolean eliminarProducte(int codi) throws SQLException {
        String query = "DELETE FROM Producte WHERE codi = ?;";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, codi);

        int count = stmt.executeUpdate();
        stmt.close();

        return count == 1;
    }

    public ArrayList<Producte> mostrarProductes() throws SQLException {
        ArrayList<Producte> prods = new ArrayList<>();
        String select = "SELECT * FROM Producte ORDER BY codi;";
        PreparedStatement stmt = conn.prepareStatement(select);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Producte p = new Producte();
            Categoria c = new Categoria();

            p.setCodi(rs.getInt("codi"));
            p.setNom(rs.getString("nom"));

            int idCategoria = rs.getInt("categoria_id");
            String catSelect = "SELECT * FROM Categoria WHERE id = ?";
            PreparedStatement catstmt = conn.prepareStatement(catSelect);
            catstmt.setInt(1, idCategoria);
            ResultSet rscat = catstmt.executeQuery();

            while (rscat.next()) {
                c.setId(idCategoria);
                c.setNom(rscat.getString("nom"));
                c.setDescripcio(rscat.getString("descripcio"));
            }

            rscat.close();
            catstmt.close();

            p.setCategoria(c);
            p.setPreu(rs.getDouble("preu"));
            p.setTipusPreu(rs.getString("tipusPreu"));
            p.setStock(rs.getInt("stock"));
            p.setOferta(rs.getBoolean("oferta"));

            prods.add(p);
        }

        return prods;
    }

    public ArrayList<Producte> mostrarProductes(String campOrdre) throws SQLException {
        ArrayList<Producte> prods = new ArrayList<>();
        campOrdre = campOrdre.toLowerCase();
        String select = "SELECT * FROM Producte ORDER BY " + campOrdre + ";";
        PreparedStatement stmt = conn.prepareStatement(select);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Producte p = new Producte();
            Categoria c = new Categoria();

            p.setCodi(rs.getInt("codi"));
            p.setNom(rs.getString("nom"));

            int idCategoria = rs.getInt("categoria_id");
            String catSelect = "SELECT * FROM Categoria WHERE id = ?";
            PreparedStatement catstmt = conn.prepareStatement(catSelect);
            catstmt.setInt(1, idCategoria);
            ResultSet rscat = catstmt.executeQuery();

            while (rscat.next()) {
                c.setId(idCategoria);
                c.setNom(rscat.getString("nom"));
                c.setDescripcio(rscat.getString("descripcio"));
            }

            rscat.close();
            catstmt.close();

            p.setCategoria(c);
            p.setPreu(rs.getDouble("preu"));
            p.setTipusPreu(rs.getString("tipusPreu"));
            p.setStock(rs.getInt("stock"));
            p.setOferta(rs.getBoolean("oferta"));

            prods.add(p);
        }

        return prods;
    }

    public ArrayList<Producte> filtrarPerCategoria(Categoria c) throws SQLException {
        ArrayList<Producte> prods = new ArrayList<>();
        String select = "SELECT * FROM Producte WHERE categoria_id = ?;";
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setInt(1, c.getId());
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Producte p = new Producte();

            p.setCodi(rs.getInt("codi"));
            p.setNom(rs.getString("nom"));
            p.setCategoria(c);
            p.setPreu(rs.getDouble("preu"));
            p.setTipusPreu(rs.getString("tipusPreu"));
            p.setStock(rs.getInt("stock"));
            p.setOferta(rs.getBoolean("oferta"));

            prods.add(p);
        }

        rs.close();
        stmt.close();

        return prods;
    }

    public ArrayList<Producte> filtrarPerStock(int maxStock) throws SQLException {
        ArrayList<Producte> prods = new ArrayList<>();
        String select = "SELECT * FROM Producte WHERE stock < ?;";
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setInt(1, maxStock);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Producte p = new Producte();

            p.setCodi(rs.getInt("codi"));
            p.setNom(rs.getString("nom"));

            int categoriaId = rs.getInt("categoria_id");
            Categoria c = CategoriaDAO.getInstance().obtenirCategoriaPerId(categoriaId); 
            p.setCategoria(c);

            p.setPreu(rs.getDouble("preu"));
            p.setTipusPreu(rs.getString("tipusPreu"));
            p.setStock(rs.getInt("stock"));
            p.setOferta(rs.getBoolean("oferta"));

            prods.add(p);
        }

        rs.close();
        stmt.close();

        return prods;
    }

    public double mostrarTotalStock () throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        double total = 0;
        String query = "SELECT SUM(preu * stock) FROM Producte";
        stmt = conn.prepareStatement(query);
        rs = stmt.executeQuery();
        
        while (rs.next()) {            
            total = rs.getInt(1);
        }
        
        rs.close();
        stmt.close();
        
        return total;
    }
}
