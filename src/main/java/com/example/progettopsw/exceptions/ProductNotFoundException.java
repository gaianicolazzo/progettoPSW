package com.example.progettopsw.exceptions;

public class ProductNotFoundException extends RuntimeException {

        public ProductNotFoundException(String message)
        {
            super(message);
        }

        public ProductNotFoundException()
        {
            this("Prodotto not found");
        }

}
