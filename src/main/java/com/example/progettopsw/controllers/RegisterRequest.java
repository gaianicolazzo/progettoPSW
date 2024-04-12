package com.example.progettopsw.controllers;

import com.example.progettopsw.modules.Role;
import lombok.Data;

@Data
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Role role;
}
