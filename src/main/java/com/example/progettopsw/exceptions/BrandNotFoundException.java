package com.example.progettopsw.exceptions;


public class BrandNotFoundException extends RuntimeException{
    public BrandNotFoundException(String message){
        super(message);
    }

    public BrandNotFoundException(){
        this("Il brand non esiste");
    }
}
