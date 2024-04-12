package com.example.progettopsw.exceptions;

public class ClientDoesntExistException extends RuntimeException {
    public ClientDoesntExistException(String message)
    {
        super(message);
        System.out.println(message);
    }
    public ClientDoesntExistException()
    {
        this("L'utente non esiste");
    }


}
