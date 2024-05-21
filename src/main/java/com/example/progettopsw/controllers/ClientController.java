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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/client")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    OrderService orderService;
    @Autowired
    ApplicationConfig applicationConfig;


    @PreAuthorize("hasAuthority('client')")
    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getOrdersList(Authentication auth){
        try{
            String email = auth.getName();
            Client c = clientService.getClientFromEmail(email);
            if (c == null) {
                return ResponseEntity.notFound().build();
            }
            List<Order> orders = orderService.getOrdini(email);

            List<OrderDTO> ret =new LinkedList<>();
            for(Order ordine : orders)
                ret.add(new OrderDTO(ordine));

            return new ResponseEntity<>(ret, HttpStatus.OK);
        }catch (ClientDoesntExistException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }


}
