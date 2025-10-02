/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author aaroncanofdez
 */
public class Producte {
    private int codi;
    private String nom;
    private Categoria categoria;
    private double preu;
    private String tipusPreu;
    private int stock;
    private boolean oferta;

    public Producte() {
    }

    public Producte(String nom, Categoria categoria, double preu, String tipusPreu, int stock, boolean oferta) {
        this.nom = nom;
        this.categoria = categoria;
        this.preu = preu;
        this.tipusPreu = tipusPreu;
        this.stock = stock;
        this.oferta = oferta;
    }

    public Producte(int codi, String nom, Categoria categoria, double preu, String tipusPreu, int stock, boolean oferta) {
        this.codi = codi;
        this.nom = nom;
        this.categoria = categoria;
        this.preu = preu;
        this.tipusPreu = tipusPreu;
        this.stock = stock;
        this.oferta = oferta;
    }

    public boolean isOferta() {
        return oferta;
    }

    public void setOferta(boolean oferta) {
        this.oferta = oferta;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public String getTipusPreu() {
        return tipusPreu;
    }

    public void setTipusPreu(String tipusPreu) {
        this.tipusPreu = tipusPreu;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producte{" + "codi=" + codi + ", nom=" + nom + ", categoria=" + categoria + ", preu=" + preu + ", tipusPreu=" + tipusPreu + ", stock=" + stock + ", oferta=" + oferta + '}';
    }
}