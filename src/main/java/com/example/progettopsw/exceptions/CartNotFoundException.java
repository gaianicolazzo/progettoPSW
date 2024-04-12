package com.example.progettopsw.exceptions;

public class CartNotFoundException extends RuntimeException{

        public CartNotFoundException(String message)
        {
            super(message);
        }
        public CartNotFoundException()
        {
            this("Carrello not found.");
        }
}

