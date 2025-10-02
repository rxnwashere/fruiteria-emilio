/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.AfegirCategoriaView;
import View.CategoriaMainView;
import View.ModificarCategoriaView;
import DAO.CategoriaDAO;
import Model.Categoria;
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
public class C_Categories implements ActionListener {

    private CategoriaMainView catv_main;
    private AfegirCategoriaView catv_afegir;
    private ModificarCategoriaView catv_modificar;

    private static C_Categories instancia = null;

    protected static Categoria c1 = new Categoria(1, "Fruits secs", "Fruits secs");
    protected static Categoria c2 = new Categoria(2, "Sucs", "Sucs de fruita");
    protected static Categoria c3 = new Categoria(3, "Fruites", "Fruites");
    protected static Categoria c4 = new Categoria(4, "Verdures", "Verdures de l'horta de l'Emilio");

    private C_Categories() {
        catv_main = new CategoriaMainView();
        catv_afegir = new AfegirCategoriaView();
        catv_modificar = new ModificarCategoriaView();

        catv_main.getAfegirButton().addActionListener(this);
        catv_main.getModificarButton().addActionListener(this);
        catv_main.getEliminarButton().addActionListener(this);
        catv_main.getProdItem1().addActionListener(this);

        catv_afegir.getAfegirButton().addActionListener(this);

        catv_modificar.getModificarButton().addActionListener(this);
    }

    public static C_Categories getInstance() {
        if (instancia == null) {
            instancia = new C_Categories();
        }
        return instancia;
    }

    public CategoriaMainView getCatv_main() {
        return catv_main;
    }

    public AfegirCategoriaView getCatv_afegir() {
        return catv_afegir;
    }

    public ModificarCategoriaView getCatv_modificar() {
        return catv_modificar;
    }

    public void run() throws SQLException {
        carregarJTable();
        catv_main.setVisible(true);
        catv_main.setLocationRelativeTo(null);
        catv_afegir.setVisible(false);
        catv_modificar.setVisible(false);
    }

    public void runAfegir() throws SQLException {
        catv_main.setVisible(false);
        catv_afegir.setVisible(true);
        catv_modificar.setVisible(false);
        catv_afegir.getWarningLabel1().setVisible(false);
        carregarJTableAfegir();
        catv_afegir.getNomField().setText(null);
        catv_afegir.getjTextField1().setText(null);
        for (ActionListener al : catv_afegir.getAfegirButton().getActionListeners()) {
            catv_afegir.getAfegirButton().removeActionListener(al);
        }

        catv_afegir.getAfegirButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nom = catv_afegir.getNomField().getText().trim();
                    catv_afegir.getWarningLabel1().setVisible(false);
                    String descripcio = catv_afegir.getjTextField1().getText().trim();

                    if (!nom.isBlank()) {
                        Categoria c = new Categoria(nom, descripcio);
                        CategoriaDAO.getInstance().afegirCategoria(c);
                        JOptionPane.showMessageDialog(catv_afegir, "Categoria afegida amb éxit!", "Categoria afegida correctament", JOptionPane.INFORMATION_MESSAGE);
                        run();
                    } else {
                        JOptionPane.showMessageDialog(catv_afegir, "El nom no pot estar buit!", "Error afegint categoria", 0);
                        catv_afegir.getWarningLabel1().setVisible(true);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(catv_afegir, "Error de base de dades!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        catv_afegir.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    run();
                } catch (SQLException ex) {
                    Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void runEliminar() throws SQLException {

        int filaSeleccionada = catv_main.getjTable1().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(catv_main, "Selecciona una categoria per eliminar.", "Advertència", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) catv_main.getjTable1().getModel();
        int idCat = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());

        int confirmacio = JOptionPane.showConfirmDialog(catv_main, "Estàs segur que vols eliminar aquesta categoria?", "Confirmar eliminació", JOptionPane.YES_NO_OPTION);
        if (confirmacio == JOptionPane.YES_OPTION) {
            if (CategoriaDAO.getInstance().isCatAssignada(idCat)) {
                JOptionPane.showMessageDialog(catv_main, "La categoria està referenciada en un o més productes! No es pot eliminar.", "Error en la eliminació!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean eliminat = CategoriaDAO.getInstance().eliminarCategoria(idCat);

            if (eliminat) {
                JOptionPane.showMessageDialog(catv_main, "Categoria eliminada amb éxit!", "Eliminació confirmada", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(catv_main, "Error en la eliminació!", "Error en la eliminació!", JOptionPane.ERROR_MESSAGE);
            }

            carregarJTable();
        }
    }

    public void runModificar() throws SQLException {
        catv_modificar.setLocationRelativeTo(null);
        int filaSeleccionada = catv_main.getjTable1().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(catv_main, "Selecciona una categoria per modificar.", "Advertència", JOptionPane.WARNING_MESSAGE);
        } else {
            DefaultTableModel taula = (DefaultTableModel) catv_main.getjTable1().getModel();
            int id = (int) taula.getValueAt(filaSeleccionada, 0);
            String nom = (String) taula.getValueAt(filaSeleccionada, 1);
            String descripcio = (String) taula.getValueAt(filaSeleccionada, 2);

            catv_modificar.setVisible(true);
            catv_afegir.setVisible(false);
            catv_main.setVisible(false);
            catv_modificar.getWarningLabel1().setVisible(false);
            carregarJTableModificar();

            catv_modificar.getNomField().setText(nom);
            catv_modificar.getjTextField1().setText(descripcio);

            for (ActionListener al : catv_modificar.getModificarButton().getActionListeners()) {
                catv_modificar.getModificarButton().removeActionListener(al);
            }

            catv_modificar.getModificarButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nomNou = catv_modificar.getNomField().getText();
                        catv_modificar.getWarningLabel1().setVisible(false);
                        String novaDesc = catv_modificar.getjTextField1().getText();

                        if (!nomNou.isBlank()) {
                            Categoria c = new Categoria(id, nomNou, novaDesc);
                            if (CategoriaDAO.getInstance().modificarCategoria(c)) {
                                JOptionPane.showMessageDialog(catv_modificar, "S'ha modificat la categoria amb èxit!", "Categoria modificada correctament", JOptionPane.INFORMATION_MESSAGE);
                                run();
                            } else {
                                JOptionPane.showMessageDialog(catv_modificar, "Error al modificar la categoria! Revisa la informació dels camps.", "Error modificant la categoria", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(catv_modificar, "El nom no pot estar buit!", "Error modificant la categoria", JOptionPane.ERROR_MESSAGE);
                            catv_modificar.getWarningLabel1().setVisible(true);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(catv_modificar, "Error de base de dades!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            catv_modificar.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        run();
                    } catch (SQLException ex) {
                        Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
    }

    public void carregarJTable() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Id", "Nom", "Descripció"
        });

        ArrayList<Categoria> cats = CategoriaDAO.getInstance().mostrarCategories();

        Object[] categories = new Object[7];

        for (Categoria c : cats) {
            categories[0] = c.getId();
            categories[1] = c.getNom();
            categories[2] = c.getDescripcio();

            tm.addRow(categories);
        }

        catv_main.getjTable1().setModel(tm);
    }

    public void carregarJTableAfegir() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Id", "Nom", "Descripció"
        });

        ArrayList<Categoria> cats = CategoriaDAO.getInstance().mostrarCategories();

        Object[] categories = new Object[7];

        for (Categoria c : cats) {
            categories[0] = c.getId();
            categories[1] = c.getNom();
            categories[2] = c.getDescripcio();

            tm.addRow(categories);
        }

        catv_afegir.getjTable1().setModel(tm);
    }

    public void carregarJTableModificar() throws SQLException {
        DefaultTableModel tm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tm.setColumnIdentifiers(new String[]{
            "Id", "Nom", "Descripció"
        });

        ArrayList<Categoria> cats = CategoriaDAO.getInstance().mostrarCategories();

        Object[] categories = new Object[7];

        for (Categoria c : cats) {
            categories[0] = c.getId();
            categories[1] = c.getNom();
            categories[2] = c.getDescripcio();

            tm.addRow(categories);
        }

        catv_modificar.getjTable1().setModel(tm);
    }

    public void canviDeVista() throws SQLException {
        catv_main.setVisible(false);
        catv_afegir.setVisible(false);
        catv_modificar.setVisible(false);
        C_Productes.getInstance().carregarJTable();
        C_Productes.getInstance().loadComponents();
        C_Productes.getInstance().prodv_main.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Afegir")) {
            try {
                runAfegir();
            } catch (SQLException ex) {
                Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Eliminar")) {
            try {
                runEliminar();
            } catch (SQLException ex) {
                Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("Modificar")) {
            try {
                runModificar();
            } catch (SQLException ex) {
                Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getActionCommand().equals("CanviarVista")) {
            try {
                canviDeVista();
            } catch (SQLException ex) {
                Logger.getLogger(C_Categories.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
