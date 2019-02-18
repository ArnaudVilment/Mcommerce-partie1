package com.ecommerce.microcommerce.model;

import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
//@JsonFilter("monFiltreDynamique")
public class Product {

    @Id
    @GeneratedValue
    private int id;

    @Length(min=3, max=20, message = "Le nom du produit doit avoir entre 3 et 20 caractères !")
    private String nom;

    private int prix;

    //information que nous ne souhaitons pas exposer
    @Min(value = 1, message = "Le prix d'achat doit etre supérieur à zéro!")
    private int prixAchat;

    //constructeur par défaut
    public Product() {
    }

    //constructeur pour nos tests
    public Product(int id, String nom, int prix, int prixAchat) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.prixAchat = prixAchat;
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

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat() {
        setPrixAchat();
    }

    public void setPrixAchat(int prixAchat) {
        this.prixAchat = prixAchat;
    }

    @ApiOperation(value = "Gérer l' affichage désiré des produits par ordre alphabétiaque.")
    @Override
    public String toString() {
        return "{\n" +
                "\"id\": " + id + ",\n" +
                "\"nom\": \"" + nom + "\"" + ",\n" +
                "\"prix\": " + prix + ",\n" +
                "\"prixAchat\": " + prixAchat + "\n" +
                '}';
    }

    @ApiOperation(value = "Gérer l' affichage des produits lors du calcul de la marge.")
    public String toStringMarge() {
        return "\"Product{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                "}\":" + (prix - prixAchat);
    }
}

