package com.ecommerce.microcommerce.web.exceptions;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api( description="Classe Exception pour gérer le cas d'un ajout de produit avec un prix de vente égale à zéro.")

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ProduitGratuitException extends RuntimeException {

    public ProduitGratuitException(String s) {
        super(s);
    }
}
