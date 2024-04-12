package com.example.progettopsw.exceptions;

public class QtyUnavaliableException extends RuntimeException {
    public QtyUnavaliableException(String message) {
        super(message);
    }

    public QtyUnavaliableException(){
        this("Not enought products in store");
    }
}
