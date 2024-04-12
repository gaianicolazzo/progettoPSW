package com.example.progettopsw.exceptions;

public class ProductAlreadyInCartException extends RuntimeException{
    public ProductAlreadyInCartException(String message){
        super(message);
    }

    public ProductAlreadyInCartException(){
        this("Product already in cart");
    }
}
