package com.example.progettopsw.modules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;

@Entity
@Table(name= "productInCart")
@Data
public class ProductInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Column(name = "id")
    private long id;


    @Basic
    @Column(name = "prize", nullable = false)
    private float prize;


    @Basic
    @Column(name = "qty",nullable = false)
    private int qty;

    @ManyToOne(optional = false)
    Product prod;


}
