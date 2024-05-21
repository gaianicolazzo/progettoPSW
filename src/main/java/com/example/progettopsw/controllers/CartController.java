package com.example.progettopsw.controllers;


import com.example.progettopsw.DTO.ProductDTO;
import com.example.progettopsw.DTO.ProductInCartDTO;
import com.example.progettopsw.exceptions.*;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.repositories.ProductInCartRepository;
import com.example.progettopsw.security.ApplicationConfig;
import com.example.progettopsw.security.JwtService;
import com.example.progettopsw.services.CartService;
import com.example.progettopsw.services.ClientService;
import com.example.progettopsw.services.OrderService;
import com.example.progettopsw.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value= "/cart")
@CrossOrigin("*")
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

    @Autowired
    JwtService jwt;

    @Autowired
    ProductInCartRepository prodInCrep;


    @GetMapping("/productsInCart")
    public ResponseEntity<List<ProductInCartDTO>> getProductsInCart(Authentication auth){
        Cart cart = null;
        try{
            String email = auth.getName();
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


    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/addProductInCart")
    public ResponseEntity<String> addProductInCart(@RequestBody ProductDTO product, Authentication auth){
        Cart cart = null;
        Product productToAdd=null;
        try{
            String email = auth.getName();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            Optional<Product> productTmp = productService.findProductByName(product.getName());
            if(!productTmp.isPresent())
                return new ResponseEntity<>("Product not present in shop", HttpStatus.NOT_FOUND);
            productToAdd = productTmp.get();
        } catch(InvalidClientException e){
            return new ResponseEntity<>("User not found "+ auth.getName(), HttpStatus.NOT_FOUND);
        }catch (ProductNotFoundException e){
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try{
            Product ret = clientService.addProduct(cart, productToAdd, product.getAvailablePz()); // richiamiamo getAvailablePz ma in realta i pezzi disponibili sono in productToAdd mentre il primo fa riferimento alla quantita che vogliamo
            return new ResponseEntity<>("Product added correctly",HttpStatus.OK);
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
    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestBody ProductDTO product, Authentication auth) {
        Cart cart = null;
        Optional<ProductInCart> productInCart;
        try {
            String email = auth.getName();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            productInCart = clientService.getProductFromCart(product.getName(), product.getColor(), cart);
            if (!productInCart.isPresent())
                return new ResponseEntity<>("Product not present in cart", HttpStatus.NOT_FOUND);
        } catch (InvalidClientException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try {
            boolean res = clientService.deleteProduct(cart, productInCart.get(), product.getAvailablePz());
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

    @PreAuthorize("hasAuthority('client')")
    @PostMapping("/addQty")
    public ResponseEntity<String> addQty(@RequestBody ProductDTO product, @RequestParam(value = "qty") int qty, Authentication auth) {
        Cart cart = null;
        Optional<ProductInCart> productInCart;
        try {
            String email = auth.getName();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            productInCart = clientService.getProductFromCart(product.getName(), product.getColor(), cart);
        } catch (InvalidClientException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
        try {
            boolean res = clientService.substitute(productInCart.get(),qty, cart);
            return res ? new ResponseEntity<>("true", HttpStatus.OK) : new ResponseEntity<>("false", HttpStatus.NOT_FOUND);
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

    @PreAuthorize("hasAuthority('client')")
    @PostMapping(value="/order")
    public ResponseEntity<String> order( Authentication auth) {
        Cart cart = null;
        List<ProductInCart> productInCart;
        try {
            String email = auth.getName();
            Client c = clientService.getClientFromEmail(email);
            cart = clientService.getCartFromClient(c);
            productInCart = prodInCrep.findAll();
            boolean res = orderService.crea(cart, productInCart, c);
            if (res) {
                return new ResponseEntity<>("true", HttpStatus.OK);
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
        return new ResponseEntity<>("Order.java of : " + productInCart + " \n failed. Try again", HttpStatus.PERMANENT_REDIRECT);
    }


}
