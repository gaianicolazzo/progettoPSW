package com.example.progettopsw.controllers;

import com.example.progettopsw.DTO.OrderDTO;
import com.example.progettopsw.exceptions.ClientDoesntExistException;
import com.example.progettopsw.modules.Client;
import com.example.progettopsw.modules.Order;
import com.example.progettopsw.security.ApplicationConfig;
import com.example.progettopsw.services.ClientService;
import com.example.progettopsw.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    OrderService orderService;
    @Autowired
    ApplicationConfig applicationConfig;


    //@PreAuthorize("hasAuthority('client')")
    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersList(){
        try{
            String email = applicationConfig.userDetailsService().toString();
            Client c = clientService.getClientFromEmail(email);
            if (c == null) {
                return ResponseEntity.notFound().build();
            }
            List<Order> orders = orderService.getOrdini(email);
            if(orders.size()==0){
                return ResponseEntity.notFound().build();
            }

            List<OrderDTO> ret =new LinkedList<>();
            for(Order ordine : orders)
                ret.add(new OrderDTO(ordine));

            return ResponseEntity.ok(ret);
        }catch (ClientDoesntExistException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }


}
