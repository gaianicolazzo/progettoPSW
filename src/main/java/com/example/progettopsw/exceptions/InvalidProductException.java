package com.example.progettopsw.exceptions;

public class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message)
    {
        super(message);
    }
    public InvalidProductException()
    {
        this("Email already used");
    }
}
