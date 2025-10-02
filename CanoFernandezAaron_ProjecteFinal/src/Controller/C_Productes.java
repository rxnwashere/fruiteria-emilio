/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import DAO.ProducteDAO;
import DAO.CategoriaDAO;
import DAO.BDUtil;
import DAO.Connection;
import Model.Producte;
import Model.Categoria;
import View.ProducteMainView;
import View.AfegirProducteView;
import View.ModificarProducteView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aaroncanofdez
 */
public class C_Productes implements ActionListener {

    protected ProducteMainView prodv_main;
    private AfegirProducteView prodv_afegir;
    private ModificarProducteView prodv_modificar;

    private static C_Productes instancia = null;

    private static Producte p1 = new Producte("Nous de Califòrnia", C_Categories.c1, 5.25, "Pes", 50, false);
    private static Producte p2 = new Producte("Ametlles torrades", C_Categories.c1, 6.80, "Pes", 30, true);
    private static Producte p3 = new Producte("Suc de taronja natural", C_Categories.c2, 1.50, "Unitari", 100, true);
    private static Producte p4 = new Producte("Suc de poma", C_Categories.c2, 1.20, "Unitari", 80, false);
    private static Producte p5 = new Producte("Plàtan de Canàries", C_Categories.c3, 1.90, "Pes", 120, false);
    private static Producte p6 = new Producte("Maduixes", C_Categories.c3, 3.50, "Paquet", 60, true);
    private static Producte p7 = new Producte("Carxofes", C_Categories.c4, 2.40, "Paquet", 40, false);
    private static Producte p8 = new Producte("Tomàquets cherry", C_Categories.c4, 2.95, "Pes", 70, true);

    private C_Productes() {
        prodv_main = new ProducteMainView();
        prodv_afegir = new AfegirProducteView();
        prodv_modificar = new ModificarProducteView();

        prodv_main.getAfegirButton().addActionListener(this);
        prodv_main.getEliminarButton().addActionListener(this);
        prodv_main.getModificarButton().addActionListener(this);
        prodv_main.getCatItem2().addActionListener(this);
        prodv_main.getNetejarItem2().addActionListener(this);
        prodv_main.getFiltrarButton().addActionListener(this);
        prodv_main.getFiltrarButton2().addActionListener(this);
        prodv_main.getOrdenarButton().addActionListener(this);
        prodv_main.getTotalitzarStockButton().addActionListener(this);

        prodv_afegir.getAfegirButton().addActionListener(this);

        prodv_modificar.getModificarButton().addActionListener(this);
    }

    public static C_Productes getInstance() {
        if (instancia == null) {
            instancia = new C_Productes();
        }
        return instancia;
    }

    public ProducteMainView getProdv_main() {
        return prodv_main;
    }

    public AfegirProducteView getProdv_afegir() {
        return prodv_afegir;
    }

    public ModificarProducteView getProdv_modificar() {
        return prodv_modificar;
    }

    public void run() throws SQLException {
        Connection.connect();
        BDUtil.createEstructuraMysql();
        CategoriaDAO.getInstance().afegirCategoria(C_Categories.c1);
        CategoriaDAO.getInstance().afegirCategoria(C_Categories.c2);
        CategoriaDAO.getInstance().afegirCategoria(C_Categories.c3);
        CategoriaDAO.getInstance().afegirCategoria(C_Categories.c4);
        ProducteDAO.getInstance().afegirProducte(p1);
        ProducteDAO.getInstance().afegirProducte(p2);
        ProducteDAO.getInstance().afegirProducte(p3);
        ProducteDAO.getInstance().afegirProducte(p4);
        ProducteDAO.getInstance().afegirProducte(p5);
        ProducteDAO.getInstance().afegirProducte(p6);
        ProducteDAO.getInstance().afegirProducte(p7);
        ProducteDAO.getInstance().afegirProducte(p8);

        carregarJTable();
        loadComponents();
        prodv_main.setVisible(true);
        prodv_main.setLocationRelativeTo(null);
        prodv_afegir.setVisible(false);
        prodv_modificar.setVisible(false);
    }

    public void hideAfegir() throws SQLException {
        prodv_afegir.setVisible(false);
        carregarJTable();
        loadComponents();
        prodv_main.setVisible(true);
    }

    public void hideModificar() throws SQLException {
        prodv_modificar.setVisible(false);
        carregarJTable();
        loadComponents();
        prodv_main.setVisible(true);
    }

    public void runAfegir() throws SQLException {
        prodv_afegir.setVisible(true);
        prodv_afegir.setLocationRelativeTo(null);
        prodv_main.setVisible(false);
        prodv_modificar.setVisible(false);
        prodv_afegir.getWarningLabel1().setVisible(false);
        prodv_afegir.getWarningLabel2().setVisible(false);
        prodv_afegir.getWarningLabel3().setVisible(false);
        prodv_afegir.getWarningLabel4().setVisible(false);

        loadComponents();
        carregarJTableAfegir();

        for (ActionListener al : prodv_afegir.getAfegirButton().getActionListeners()) {
            prodv_afegir.getAfegirButton().removeActionListener(al);
        }

        prodv_afegir.getAfegirButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = prodv_afegir.getNomField().getText().trim();
                    prodv_afegir.getWarningLabel1().setVisible(false);
                    if (nom.isBlank()) {
                        JOptionPane.showMessageDialog(prodv_afegir, "El camp de nom no pot quedar buit!", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                        prodv_afegir.getWarningLabel1().setVisible(true);
                        return;
                    }

                    String catNom = (String) prodv_afegir.getCategoriesComboBox().getSelectedItem();
                    int idCat = CategoriaDAO.getInstance().returnId(catNom);
                    Categoria categoria = CategoriaDAO.getInstance().obtenirCategoriaPerId(idCat);

                    double preu = 0;
                    try {
                        preu = Double.parseDouble(prodv_afegir.getjTextField1().getText().trim());
                        prodv_afegir.getWarningLabel2().setVisible(false);
                        if (preu <= 0) {
                            JOptionPane.showMessageDialog(prodv_afegir, "El camp de preu ha de ser un número vàlid.", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_afegir.getWarningLabel2().setVisible(true);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(prodv_afegir, "El camp de preu ha de ser un número vàlid.", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                        prodv_afegir.getWarningLabel2().setVisible(true);
                        return;
                    }

                    String tipusPreu = null;
                    prodv_afegir.getWarningLabel4().setVisible(false);
                    if (prodv_afegir.getjRadioButton1().isSelected()) {
                        tipusPreu = "Pes";
                    } else if (prodv_afegir.getjRadioButton2().isSelected()) {
                        tipusPreu = "Unitari";
                    } else if (prodv_afegir.getjRadioButton3().isSelected()) {
                        tipusPreu = "Paquet";
                    } else {
                        JOptionPane.showMessageDialog(prodv_afegir, "No s'ha seleccionat un tipus de preu!", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                        prodv_afegir.getWarningLabel4().setVisible(true);
                        return;
                    }

                    int stock = 0;
                    try {
                        stock = Integer.parseInt(prodv_afegir.getStockField().getText().trim());
                        prodv_afegir.getWarningLabel3().setVisible(false);
                        if (stock < 0) {
                            JOptionPane.showMessageDialog(prodv_afegir, "El camp stock ha de ser un número vàlid.", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_afegir.getWarningLabel3().setVisible(true);
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(prodv_afegir, "El camp stock ha de ser un número vàlid.", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                        prodv_afegir.getWarningLabel3().setVisible(true);
                        return;
                    }

                    boolean oferta = prodv_afegir.getOfertaChekBox().isSelected();

                    if (!nom.isBlank() && preu > 0 && stock >= 0) {
                        Producte p = new Producte(nom, categoria, preu, tipusPreu, stock, oferta);
                        if (ProducteDAO.getInstance().afegirProducte(p)) {
                            JOptionPane.showMessageDialog(prodv_afegir, "S'ha afegit el producte amb èxit!", "Producte afegit correctament", JOptionPane.INFORMATION_MESSAGE);
                            hideAfegir();
                        } else {
                            JOptionPane.showMessageDialog(prodv_afegir, "Error a l'afegir el producte! Revisa la informació dels camps.", "Error afegint el producte", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(prodv_afegir, "Error de base de dades!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        prodv_afegir.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                prodv_main.setVisible(true);
            }
        });

    }

    public void runModificar() throws SQLException {

        int filaSeleccionada = prodv_main.getjTable1().getSelectedRow();
        loadComponents();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(prodv_main, "Selecciona un producte per modificar.", "Advertència", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel taula = (DefaultTableModel) prodv_main.getjTable1().getModel();
            int codi = (int) taula.getValueAt(filaSeleccionada, 0);
            String nom = (String) taula.getValueAt(filaSeleccionada, 1);
            String categoriaNom = (String) taula.getValueAt(filaSeleccionada, 2);
            double preu = (double) taula.getValueAt(filaSeleccionada, 3);
            String tipusPreu = (String) taula.getValueAt(filaSeleccionada, 4);
            int stock = (int) taula.getValueAt(filaSeleccionada, 5);
            boolean oferta = (boolean) taula.getValueAt(filaSeleccionada, 6);

            prodv_modificar.getNomField().setText(nom);

            prodv_modificar.getCategoriesComboBox().setSelectedItem(categoriaNom);

            prodv_modificar.getjTextField1().setText(String.valueOf(preu));

            if (tipusPreu.equals("Pes")) {
                prodv_modificar.getjRadioButton1().setSelected(true);
            } else if (tipusPreu.equals("Unitari")) {
                prodv_modificar.getjRadioButton2().setSelected(true);
            } else if (tipusPreu.equals("Paquet")) {
                prodv_modificar.getjRadioButton3().setSelected(true);
            }

            prodv_modificar.getStockField().setText(String.valueOf(stock));

            prodv_modificar.getOfertaChekBox().setSelected(oferta);

            prodv_modificar.setVisible(true);
            prodv_modificar.setLocationRelativeTo(null);
            prodv_afegir.setVisible(false);
            prodv_main.setVisible(false);
            prodv_modificar.getWarningLabel1().setVisible(false);
            prodv_modificar.getWarningLabel2().setVisible(false);
            prodv_modificar.getWarningLabel3().setVisible(false);
            prodv_modificar.getWarningLabel4().setVisible(false);

            carregarJTableModificar();

            for (ActionListener al : prodv_modificar.getModificarButton().getActionListeners()) {
                prodv_modificar.getModificarButton().removeActionListener(al);
            }

            prodv_modificar.getModificarButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nomNou = prodv_modificar.getNomField().getText();
                        prodv_modificar.getWarningLabel1().setVisible(false);
                        if (nomNou.isBlank()) {
                            JOptionPane.showMessageDialog(prodv_afegir, "El camp de nom no pot quedar buit!", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_modificar.getWarningLabel1().setVisible(true);
                            return;
                        }

                        String catNom = (String) prodv_modificar.getCategoriesComboBox().getSelectedItem();
                        int idCat = CategoriaDAO.getInstance().returnId(catNom);
                        Categoria categoria = CategoriaDAO.getInstance().obtenirCategoriaPerId(idCat);

                        double nouPreu = 0;
                        try {
                            nouPreu = Double.parseDouble(prodv_modificar.getjTextField1().getText());
                            prodv_modificar.getWarningLabel2().setVisible(false);
                            if (nouPreu <= 0) {
                                JOptionPane.showMessageDialog(prodv_modificar, "El camp de preu ha de ser un número vàlid.", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                                prodv_modificar.getWarningLabel2().setVisible(true);
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(prodv_modificar, "El camp de preu ha de ser un número vàlid.", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_modificar.getWarningLabel2().setVisible(true);
                            return;
                        }

                        String nouTipusPreu = null;
                        prodv_modificar.getWarningLabel4().setVisible(false);
                        if (prodv_modificar.getjRadioButton1().isSelected()) {
                            nouTipusPreu = "Pes";
                        } else if (prodv_modificar.getjRadioButton2().isSelected()) {
                            nouTipusPreu = "Unitari";
                        } else if (prodv_modificar.getjRadioButton3().isSelected()) {
                            nouTipusPreu = "Paquet";
                        } else {
                            JOptionPane.showMessageDialog(prodv_modificar, "No s'ha seleccionat un tipus de preu!", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_modificar.getWarningLabel4().setVisible(true);
                            return;
                        }

                        int nouStock = 0;
                        try {
                            nouStock = Integer.parseInt(prodv_modificar.getStockField().getText());
                            prodv_modificar.getWarningLabel3().setVisible(false);
                            if (nouStock < 0) {
                                JOptionPane.showMessageDialog(prodv_modificar, "El camp stock ha de ser un número vàlid.", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                                prodv_modificar.getWarningLabel3().setVisible(true);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(prodv_modificar, "El camp stock ha de ser un número vàlid.", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                            prodv_modificar.getWarningLabel3().setVisible(true);
                            return;
                        }

                        boolean novaOferta = prodv_modificar.getOfertaChekBox().isSelected();

                        if (!nomNou.isBlank() && nouPreu > 0 && nouStock >= 0) {
                            Producte p = new Producte(codi, nomNou, categoria, nouPreu, nouTipusPreu, nouStock, novaOferta);
                            if (ProducteDAO.getInstance().modificarProducte(p)) {
                                JOptionPane.showMessageDialog(prodv_modificar, "S'ha modificat el producte amb èxit!", "Producte modificat correctament", JOptionPane.INFORMATION_MESSAGE);
                                hideModificar();
                            } else {
                                JOptionPane.showMessageDialog(prodv_modificar, "Error al modificar el producte! Revisa la informació dels camps.", "Error modificant el producte", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(prodv_modificar, "Error de base de dades!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            prodv_modificar.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    prodv_main.setVisible(true);
                }
            });
        }
    }

    public void runEliminar() throws SQLException {

        int filaSeleccionada = prodv_main.getjTable1().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(prodv_main, "Selecciona un producte per eliminar.", "Advertència", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) prodv_main.getjTable1().getModel();
        int codiProducte = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());

        int confirmacio = JOptionPane.showConfirmDialog(prodv_main, "Estàs segur que vols eliminar aquest producte?", "Confirmar eliminació", JOptionPane.YES_NO_OPTION);
        if (confirmacio == JOptionPane.YES_OPTION) {
            boolean eliminat = ProducteDAO.getInstance().eliminarProducte(codiProducte);

            if (eliminat) {
                JOptionPane.showMessageDialog(prodv_main, "Producte eliminat amb éxit!", "Eliminació confirmada", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(prodv_main, "Error en la eliminació!", "Error en la eliminació!", JOptionPane.ERROR_MESSAGE);
            }

            carregarJTable();
        }
    }

    public void runFiltrarCategoria() throws SQLException {
        String catNom = (String) prodv_main.getCategoriesComboBox().getSelectedItem();
        int idCat = CategoriaDAO.getInstance().returnId(catNom);
        Categoria categoria = CategoriaDAO.getInstance().obtenirCategoriaPerId(idCat);

        ArrayList<Producte> productesFiltrats = ProducteDAO.getInstance().filtrarPerCategoria(categoria);

        DefaultTableModel model = (DefaultTableModel) prodv_main.getjTable1().getModel();
        model.setRowCount(0);

        for (Producte p : productesFiltrats) {
            Object[] row = new Object[]{
                p.getCodi(),
                p.getNom(),
                p.getCategoria().getNom(),
                p.getPreu(),
                p.getTipusPreu(),
                p.getStock(),
                p.isOferta()
            };
            model.addRow(row);
        }
    }

    public void runFiltrarPerStockMenor() throws SQLException {
        int stockAFiltrar;

        try {
            stockAFiltrar = Integer.parseInt(prodv_main.getStockField().getText());
            ArrayList<Producte> productesFiltrats = ProducteDAO.getInstance().filtrarPerStock(stockAFiltrar);

            DefaultTableModel model = (DefaultTableModel) prodv_main.getjTable1().getModel();
            model.setRowCount(0);

            for (Producte p : productesFiltrats) {
                Object[] row = new Object[]{
                    p.getCodi(),
                    p.getNom(),
                    p.getCategoria().getNom(),
                    p.getPreu(),
                    p.getTipusPreu(),
                    p.getStock(),
                    p.isOferta()
                };
                model.addRow(row);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(prodv_modificar, "El camp stock ha de ser un número vàlid.", "Error filtrant", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void runOrdenar() throws SQLException {
        String campOrdenar = (String) prodv_main.getjComboBox1().getSelectedItem();
        ArrayList<Producte> prods = null;

        switch (campOrdenar.toLowerCase()) {
            case "codi":
                prods = ProducteDAO.getInstance().mostrarProductes("codi");
                break;
            case "nom":
                prods = ProducteDAO.getInstance().mostrarProductes("nom");
                break;
            case "categoria":
                prods = ProducteDAO.getInstance().mostrarProductes("categoria_id");
                break;
            case "preu":
                prods = ProducteDAO.getInstance().mostrarProductes("preu");
                break;
            case "tipus preu":
                prods = ProducteDAO.getInstance().mostrarProductes("tipusPreu");
                break;
            case "stock":
                prods = ProducteDAO.getInstance().mostrarProductes("stock");
                break;
            case "oferta":
                prods = ProducteDAO.getInstance().mostrarProductes("oferta");
                break;
            default:
                break;
        }

        DefaultTableModel model = (DefaultTableModel) prodv_main.getjTable1().getModel();
        model.setRowCount(0);

        if (prods != null) {
            for (Producte p : prods) {
                Object[] row = new Object[]{
                    p.getCodi(),
                    p.getNom(),
                    p.getCategoria().getNom(),
                    p.getPreu(),
                    p.getTipusPreu(),
                    p.getStock(),
                    p.isOferta()
                };
                model.addRow(row);
            }
        } else {
            JOptionPane.showMessageDialog(prodv_main, "No s'ha pogut obtenir la llista de productes.", "Error ordenant!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void runMostrarStockTotal() throws SQLException {
        JOptionPane.showMessageDialog(prodv_main, ProducteDAO.getInstance().mostrarTotalStock() + "€", "Totalització stock", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadComponents() throws SQLException {
        ArrayList<Categoria> cats = CategoriaDAO.getInstance().mostrarCategories();

        prodv_afegir.getCategoriesComboBox().removeAllItems();

        for (int i = 0; i < cats.size(); i++) {
            prodv_afegir.getCategoriesComboBox().addItem(cats.get(i).getNom());
        }

        prodv_modificar.getCategoriesComboBox().removeAllItems();

        for (int i = 0; i < cats.size(); i++) {
            prodv_modificar.getCategoriesComboBox().addItem(cats.get(i).getNom());
        }

        prodv_main.getCategoriesComboBox().removeAllItems();
        for (int i = 0; i < cats.size(); i++) {
            prodv_main.getCategoriesComboBox().addItem(cats.get(i).getNom());
        }

        prodv_main.getjComboBox1().removeAllItems();
        prodv_main.getjComboBox1().addItem("Codi");
        prodv_main.getjComboBox1().addItem("Nom");
        prodv_main.getjComboBox1().addItem("Categoria");
        prodv_main.getjComboBox1().addItem("Preu");
        prodv_main.getjComboBox1().addItem("Tipus Preu");
        prodv_main.getjComboBox1().addItem("Stock");
        prodv_main.getjComboBox1().addItem("Oferta");
    }

    public void carregarJTable() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Codi", "Nom", "Categoria", "Preu", "Tipus Preu", "Stock", "Oferta"
        });

        ArrayList<Producte> prods = ProducteDAO.getInstance().mostrarProductes();

        Object[] productes = new Object[7];

        for (Producte p : prods) {
            productes[0] = p.getCodi();
            productes[1] = p.getNom();
            productes[2] = p.getCategoria().getNom();
            productes[3] = p.getPreu();
            productes[4] = p.getTipusPreu();
            productes[5] = p.getStock();
            productes[6] = p.isOferta();

            tm.addRow(productes);
        }

        prodv_main.getjTable1().setModel(tm);
    }

    public void carregarJTableAfegir() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Codi", "Nom", "Categoria", "Preu", "Tipus Preu", "Stock", "Oferta"
        });

        ArrayList<Producte> prods = ProducteDAO.getInstance().mostrarProductes();

        Object[] productes = new Object[7];

        for (Producte p : prods) {
            productes[0] = p.getCodi();
            productes[1] = p.getNom();
            productes[2] = p.getCategoria().getNom();
            productes[3] = p.getPreu();
            productes[4] = p.getTipusPreu();
            productes[5] = p.getStock();
            productes[6] = p.isOferta();

            tm.addRow(productes);
        }

        prodv_afegir.getjTable1().setModel(tm);
    }

    public void carregarJTableModificar() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Codi", "Nom", "Categoria", "Preu", "Tipus Preu", "Stock", "Oferta"
        });

        ArrayList<Producte> prods = ProducteDAO.getInstance().mostrarProductes();

        Object[] productes = new Object[7];

        for (Producte p : prods) {
            productes[0] = p.getCodi();
            productes[1] = p.getNom();
            productes[2] = p.getCategoria().getNom();
            productes[3] = p.getPreu();
            productes[4] = p.getTipusPreu();
            productes[5] = p.getStock();
            productes[6] = p.isOferta();

            tm.addRow(productes);
        }

        prodv_modificar.getjTable1().setModel(tm);
    }

    public void canviDeVista() throws SQLException {
        prodv_main.setVisible(false);
        prodv_afegir.setVisible(false);
        prodv_modificar.setVisible(false);
        C_Categories.getInstance().run();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Eliminar")) {
            try {
                runEliminar();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (e.getActionCommand().equals("Afegir")) {
            try {
                runAfegir();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Modificar")) {
            try {
                runModificar();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("FiltrarCat")) {
            try {
                runFiltrarCategoria();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("FiltrarStock")) {
            try {
                runFiltrarPerStockMenor();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Ordenar")) {
            try {
                runOrdenar();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Mostrar stock total")) {
            try {
                runMostrarStockTotal();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Netejar filtres")) {
            try {
                carregarJTable();
                loadComponents();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("CanviarVista")) {
            try {
                canviDeVista();
            } catch (SQLException ex) {
                Logger.getLogger(C_Productes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
