package com.example.progettopsw.modules;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_order")
public class Order {
    @JsonIgnore
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    @JsonIgnore
    @Column(name = "orderDate")
    private Date creatDate;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Client client;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private Collection<OrderDetail> details = new LinkedList<>();
    @Version
    private long version;

}

