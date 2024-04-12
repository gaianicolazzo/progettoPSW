package com.example.progettopsw.services;

import com.example.progettopsw.exceptions.CartNotFoundException;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.repositories.CartRepository;
import com.example.progettopsw.repositories.ProductInCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ProductInCartRepository prodincartrep;

    @Autowired
    private CartRepository cartrep;

    @Transactional(readOnly = true)
    public List<ProductInCart> showAllProductsInCart(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<ProductInCart> products = prodincartrep.findAll(paging);
        if (products.hasContent())
            return products.getContent();
        return new ArrayList<>();
    }


    @Transactional(readOnly = true)
    public Optional<ProductInCart> getProductInCart(String productName,String color ,Cart cart){
        if( productName == null)
            throw new IllegalArgumentException();
        if(cart == null)
            throw new CartNotFoundException();
        Optional<Cart> foundCart = cartrep.findById(cart.getId());
        if(foundCart.isEmpty()) return Optional.empty();
        for(ProductInCart pc : foundCart.get().getProducts()){
            Optional<ProductInCart> foundProduct = prodincartrep.findById(pc.getId());
            if(foundProduct.isPresent())
                if(pc.getProd().getName().toLowerCase().equals(productName) && pc.getProd().getColor().equals(color))
                    return Optional.of(foundProduct.get());

        }
        return Optional.empty();
    }


}
