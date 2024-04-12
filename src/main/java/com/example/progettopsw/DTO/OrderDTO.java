package com.example.progettopsw.DTO;

import com.example.progettopsw.modules.Order;
import com.example.progettopsw.modules.OrderDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class OrderDTO {

    private Date date;
    private List<ProductInCartDTO> addedProducts = new LinkedList<>();

    public OrderDTO(Order order){
        date=order.getCreatDate();
        for(OrderDetail orderProduct : order.getDetails())
            addedProducts.add(new ProductInCartDTO(orderProduct));
    }
}
