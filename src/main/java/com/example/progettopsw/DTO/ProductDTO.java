package com.example.progettopsw.DTO;

import com.example.progettopsw.modules.Product;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String name;

    private String barCode;

    private float prize;

    private int availablePz;

    private Date creatData;

    private String descr;

    private String color;

    private String category;

    private String brand;

    public ProductDTO(Product product){
        name = product.getName();
        prize = product.getPrize();
        availablePz=product.getAvailablePz();
        creatData=product.getCreatData();
        descr=product.getDescr();
        color=product.getColor();
        category= product.getCategory();
        barCode= product.getBarCode();
    }


}
