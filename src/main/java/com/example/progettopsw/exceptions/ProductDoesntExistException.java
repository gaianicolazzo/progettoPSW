package com.example.progettopsw.exceptions;


public class ProductDoesntExistException extends RuntimeException {
    public ProductDoesntExistException(String message){
        super(message);
    }

    public ProductDoesntExistException(){
        this("Il prodotto non esiste");
    }
}
