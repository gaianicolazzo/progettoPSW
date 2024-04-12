package com.example.progettopsw.exceptions;

public class BarCodeAlrExistException extends RuntimeException {
    public BarCodeAlrExistException(String message){
        super(message);
    }

    public BarCodeAlrExistException(){
        this("Il prodotto esiste già");
    }
}
