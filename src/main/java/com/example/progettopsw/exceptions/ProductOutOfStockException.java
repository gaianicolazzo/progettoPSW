package com.example.progettopsw.exceptions;

import com.example.progettopsw.modules.Product;

public class ProductOutOfStockException extends RuntimeException{
    public ProductOutOfStockException(String message){
        super(message);
    }

    public ProductOutOfStockException(){
        this("Product out of stock.");
    }
}
