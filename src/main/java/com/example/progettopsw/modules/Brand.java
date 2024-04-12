package com.example.progettopsw.modules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedList;

@Entity
@Data
@Table(name = "brand")
public class Brand {
    @JsonIgnore
    @GeneratedValue
    @Id
    private Long id;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "brand", cascade= CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private Collection<Product> products = new LinkedList<>();

    public Brand(){}

    public Brand(Brand brand)
    {
        name=brand.name.toLowerCase();
        products=new LinkedList<Product>();
    }
    public Brand(String nome)
    {
        this.name=nome;
        products=new LinkedList<Product>();
    }

}