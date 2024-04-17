package com.example.progettopsw.DTO;

import com.example.progettopsw.modules.OrderDetail;
import com.example.progettopsw.modules.ProductInCart;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCartDTO {

    private double prize;
    private ProductDTO product;
    private int qty;

    public ProductInCartDTO(ProductInCart product){
        prize = product.getPrize();
        qty = product.getQty();
        this.product = new ProductDTO(product.getProduct());
    }

    public ProductInCartDTO(OrderDetail product){
        prize=product.getPrize();
        qty = product.getQty();
        this.product = new ProductDTO(product.getProduct());
    }
}
