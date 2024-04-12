package com.example.progettopsw.exceptions;

public class ProductAlreadyExistsException extends RuntimeException{
    public ProductAlreadyExistsException(String message)
    {
        super(message);
    }

    public ProductAlreadyExistsException()
    {
        this("Prodotto already exists");
    }
}
