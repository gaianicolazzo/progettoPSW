package com.example.progettopsw.controllers;


import com.example.progettopsw.DTO.ProductDTO;
import com.example.progettopsw.DTO.ProductInCartDTO;
import com.example.progettopsw.exceptions.*;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.security.ApplicationConfig;
import com.example.progettopsw.services.CartService;
import com.example.progettopsw.services.ClientService;
import com.example.progettopsw.services.OrderService;
import com.example.progettopsw.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value= "/cart")
public class CartController {
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Autowired
    ClientService clientService;
    @Autowired
    OrderService orderService;
    @Autowired
    ApplicationConfig applicationConfig;

    @GetMapping
    public ResponseEntity<List<ProductInCartDTO>> getProductsInCart(){
        Cart cart = null;
        try{
            String email = applicationConfig.userDetailsService().toString();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
        } catch (ClientDoesntExistException | InvalidClientException e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        List<ProductInCart> product = clientService.showAllProductsInCart();
        List<ProductInCartDTO> ret =new LinkedList<>();
        for(ProductInCart prod : product)
            ret.add(new ProductInCartDTO(prod));
        if(product.size()<=0)
            return new ResponseEntity<>(ret, HttpStatus.OK);
        else
            return new ResponseEntity<>(ret, HttpStatus.OK);
    }


    //@PreAuthorize("hasAuthority('utente')")
    @PostMapping
    public ResponseEntity<String> addProductInCart(@RequestBody ProductInCartDTO form){
        Cart cart = null;
        Product productToAdd=null;
        try{
            String email = applicationConfig.userDetailsService().toString();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            Optional<Product> productTmp = productService.showProductsByNameandByCategoryandByColor(form.getProduct().getName(),form.getProduct().getCategory(),form.getProduct().getColor());
            if(!productTmp.isPresent())
                return new ResponseEntity<>("Product not present in shop", HttpStatus.NOT_FOUND);
            productToAdd = productTmp.get();
        } catch(InvalidClientException e){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }catch (ProductNotFoundException e){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try{
            Product ret = clientService.addProduct(cart, productToAdd, form.getQty());
            return new ResponseEntity<>(ret.toString(),HttpStatus.OK);
        }catch(InvalidProductException ipe){
            return new ResponseEntity<>("Invalid product", HttpStatus.NOT_ACCEPTABLE);
        }catch(ProductOutOfStockException poose){
            return new ResponseEntity<>("Product out of stock", HttpStatus.MOVED_PERMANENTLY);
        }catch(CartNotFoundException cnfe){
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }catch(QtyUnavaliableException nepe){
            return new ResponseEntity<>("Select less quantity", HttpStatus.PAYLOAD_TOO_LARGE);
        }catch(ProductAlreadyInCartException paice)
        {
            return new ResponseEntity<>("Product already in cart", HttpStatus.NOT_ACCEPTABLE);
        }catch(OptimisticLockingFailureException olfe)
        {
            return new ResponseEntity<>("Operation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //@PreAuthorize("hasAuthority('utente')")
    @PostMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestBody ProductDTO product) {
        Cart cart = null;
        Optional<ProductInCart> productInCart;
        try {
            String email = applicationConfig.userDetailsService().toString();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            productInCart = cartService.getProductInCart(product.getName(), product.getColor(), cart);
            if (!productInCart.isPresent())
                return new ResponseEntity<>("Product not present in cart", HttpStatus.NOT_FOUND);
        } catch (InvalidClientException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try {
            boolean res = clientService.deleteProduct(cart, productInCart.get(), productInCart.get().getQty());
            return res ? new ResponseEntity<>(String.valueOf(true), HttpStatus.OK) : new ResponseEntity<>(String.valueOf(false), HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException pnfe) {
            return new ResponseEntity<>("Product already cancelled", HttpStatus.NOT_FOUND);
        } catch (CartNotFoundException cnfe) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (InvalidProductException ipe) {
            return new ResponseEntity<>("Product not in cart", HttpStatus.NOT_FOUND);
        } catch (OptimisticLockingFailureException olfe) {
            return new ResponseEntity<>("Operation not completed ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //@PreAuthorize("hasAuthority('client')")
    @PostMapping(value="/order")
    public ResponseEntity<String> order(@RequestBody List<ProductInCartDTO> products) {
        Cart cart = null;
        Optional<ProductInCart> productInCart;
        try {
            String email = applicationConfig.userDetailsService().toString();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            boolean res = orderService.crea(cart, products, c);
            if (res) {
                return new ResponseEntity<>("Order.java completed correctly", HttpStatus.OK);
            }
        } catch (CartNotFoundException cnfe) {
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        } catch (ProductOutOfStockException | PrizeChangedException poos) {
            return new ResponseEntity<>(poos.getMessage(), HttpStatus.NOT_FOUND);
        } catch (QtyUnavaliableException que) {
            return new ResponseEntity<>("Not enough pieces of type: " + que.getMessage(), HttpStatus.NOT_FOUND);
        } catch (OptimisticLockingFailureException olfe) {
            return new ResponseEntity<>("Operation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }//try catch
        return new ResponseEntity<>("Order.java of : " + products + " \n failed. Try again", HttpStatus.PERMANENT_REDIRECT);
    }


}
