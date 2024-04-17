package com.example.progettopsw.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_detail")
public class OrderDetail {
    @JsonIgnore
    @GeneratedValue
    @Id
    private Long id;

    @Basic
    @Column(name = "qty")
    private int qty;

    @Basic
    @Column(name = "prize")
    private double prize;
    @ManyToOne(optional = false)
    private Product product;
    @JsonIgnore
    @Version
    private long version;


}
