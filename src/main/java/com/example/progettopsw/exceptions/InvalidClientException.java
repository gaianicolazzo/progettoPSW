package com.example.progettopsw.exceptions;

public class InvalidClientException extends RuntimeException {
    public InvalidClientException(String message){
        super(message);
    }

    public InvalidClientException(){
        this("Invalid Client");
    }
}
