package com.example.progettopsw.exceptions;

public class ClientDoesntExistException extends RuntimeException {
    public ClientDoesntExistException(String message)
    {
        super(message);
    }
    public ClientDoesntExistException()
    {
        this("L'utente non esiste");
    }
}
