package com.example.progettopsw.modules;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name= "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {


    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @NotBlank
    @Column(name = "name")
    private String name;

    @Basic
    @NotBlank
    @Column(name = "barCode")
    private String barCode;
    @Basic
    @Column(name = "prize", nullable = false)
    private float prize;


    @Basic
    @Column(name = "available",nullable = false)
    private int availablePz;


    @Basic
    @Column(name = "creatData", nullable = false)
    private Date creatData;

    @Basic
    @NotBlank
    @Column(name = "description")
    private String descr;

    @Basic
    @NotBlank
    @Column(name = "color")
    private String color;

    @Basic
    @NotBlank
    @Column(name = "category")
    private String category;



    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    @ManyToOne(optional = false, cascade=CascadeType.ALL)
    private Brand brand;


}
