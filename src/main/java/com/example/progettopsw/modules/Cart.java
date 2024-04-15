package com.example.progettopsw.modules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {


    @Id
    @GeneratedValue
    private long id;

    @Basic
    @Column(name = "qty", nullable = false)
    private int qta;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductInCart> products = new LinkedList<>();

    @OneToOne(optional = false)
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ToString.Exclude
    @JsonIgnore
    private Client client;


}