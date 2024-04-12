package com.example.progettopsw.exceptions;

public class BrandAlreadyExistsException extends RuntimeException {

    public BrandAlreadyExistsException(String message){
        super(message);
    }
    public BrandAlreadyExistsException(){
        this("Brand already exist");
    }
}
