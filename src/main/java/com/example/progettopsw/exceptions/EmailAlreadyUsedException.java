package com.example.progettopsw.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message)
    {
        super(message);
    }
    public EmailAlreadyUsedException()
    {
        this("Email already used");
    }
}
