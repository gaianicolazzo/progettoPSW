package com.example.progettopsw.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String message){
        super(message);
    }

    public UsernameNotFoundException(){
        this("Email non trovata");
    }
}
