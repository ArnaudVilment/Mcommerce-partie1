package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api( description="API pour des opérations CRUD sur les produits.")

@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    //Récupérer la liste des produits
    @GetMapping(value = "/Produits")
    public MappingJacksonValue listeProduits() {

        Iterable<Product> produits = productDao.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");

        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);

        produitsFiltres.setFilters(listDeNosFiltres);

        return produitsFiltres;
    }

    //Récupérer un produit par son Id
    @ApiOperation(value = "Récupère un produit grâce à son ID à condition que celui-ci soit en stock!")
    @GetMapping(value = "/Produits/{id}")
    public Product afficherUnProduit(@PathVariable int id) {

        Product produit = productDao.findById(id);

        if(produit == null) throw new ProduitIntrouvableException("Le produit avec l'id " + id + " est INTROUVABLE !");

        return produit;
    }

    //ajouter un produit
    @ApiOperation(value = "Ajout d'un produit à condition que le prix de vente soit supérieur à 0.")
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        if(product.getPrix() == 0) {
            throw new ProduitGratuitException("Le prix de vente du produit doit être supérieur à zéro !");
        }
        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @DeleteMapping(value = "/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {

        productDao.delete(id);
    }

    // Modifier un produit
    @PutMapping (value = "/Produits")
    public void updateProduit(@RequestBody Product product) {

        productDao.save(product);
    }

    @ApiOperation(value = "Calcul la marge de chaque produit en base de donnée et affiche le résultat.")
    @GetMapping(value = "/AdminProduits")
    public String calculerMargeProduit() {

        String chaine = "{\n";
        List<Product> listProduit =  productDao.findAll();
        for(int i = 0; i < listProduit.size(); i++) {

            if(i == listProduit.size() - 1)
                chaine = chaine + listProduit.get(i).toStringMarge() + "\n}";
            else if(chaine.equals("{\n"))
                chaine = chaine + listProduit.get(i).toStringMarge() + ",\n";
            else if(!chaine.equals(""))
                chaine = chaine + listProduit.get(i).toStringMarge() + ",\n";
         }
        return chaine;
    }

    @ApiOperation(value = "Affichage des produits par ordre alphabétiaque.")
    @GetMapping(value = "/ProduitTriAsc")
    public String trierProduitsParOrdreAlphabetique() {

        String chaine = "{\n";
        List<Product> listProduit =  productDao.findAllByOrderByNom();
        for(int i = 0; i < listProduit.size(); i++) {

            if(i == listProduit.size() - 1)
                chaine = chaine + listProduit.get(i).toString() + "\n}";
            else if(chaine.equals("{\n"))
                chaine = chaine + listProduit.get(i).toString() + ",\n";
            else if(!chaine.equals(""))
                chaine = chaine + listProduit.get(i).toString() + ",\n";
        }
        return chaine;
    }

    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product> testeDeRequetes(@PathVariable int prix) {

        return productDao.chercherUnProduitCher(400);
    }
}
