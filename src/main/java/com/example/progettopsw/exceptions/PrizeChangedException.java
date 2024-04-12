package com.example.progettopsw.exceptions;

public class PrizeChangedException extends RuntimeException {
    public PrizeChangedException(String message) {
        super(message);
    }

    public PrizeChangedException()
    {
        this("Prezzo cambiato exception");
    }


}
