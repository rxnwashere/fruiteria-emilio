/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author aaroncanofdez
 */
public class Categoria {
    private int id;
    private String nom;
    private String descripcio;

    public Categoria() {
    }

    public Categoria(String nom, String descripcio) {
        this.nom = nom;
        this.descripcio = descripcio;
    }

    public Categoria(int id, String nom, String descripcio) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Categoria{" + "id=" + id + ", nom=" + nom + ", descripcio=" + descripcio + '}';
    }
}