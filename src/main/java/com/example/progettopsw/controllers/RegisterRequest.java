package com.example.progettopsw.controllers;

import com.example.progettopsw.modules.Client;
import com.example.progettopsw.modules.Product;
import com.example.progettopsw.modules.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;



}
