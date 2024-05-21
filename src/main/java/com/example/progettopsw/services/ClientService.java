package com.example.progettopsw.services;

import com.example.progettopsw.exceptions.*;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
public class ClientService {
    @Autowired
    private ClientRepository cr;


    @Autowired
    private CartRepository carep;

    @Autowired
    private ProductRepository prep;

    @Autowired
    private ProductInCartRepository prodincrep;

    @Autowired
    ClientOrderRepository orderep;


    @Transactional(readOnly = true)
    public List<Client> showAllClients(){
        return cr.findAll();
    }

    public List<Client> showClientsByName(String fName){
        return cr.findByFirstName(fName);
    }

    public List<Client> showClientsByFNameAndLName(String fName, String lName){
        return cr.findByFirstNameAndLastName(fName,lName);
    }

    public List<Client> showClientsByLastName(String lName){
        return cr.findByLastName(lName);
    }


    public void update(long id, Client c){
        Optional<Client> foundClient = cr.findById(id);
        if(foundClient.isPresent()) {
            foundClient.get().setEmail(c.getEmail());
            foundClient.get().setFirstName(c.getFirstName());
            foundClient.get().setLastName(c.getLastName());
            foundClient.get().setVersion(foundClient.get().getVersion()+1);
        }
        throw new ClientDoesntExistException();
    }

    public boolean delete(long id){
        Optional<Client> foundClient = cr.findById(id);
        if(foundClient.isPresent()) {
            cr.delete(foundClient.get());
            return true;
        }
        return false;
    }

    public boolean deleteProduct(Cart cart, ProductInCart prod, int qty){
        if(cart == null)
            throw new CartNotFoundException();
        if(prod == null)
            throw new ProductNotFoundException();


        Optional<Cart> foundCart= carep.findById(cart.getId());

        Optional<ProductInCart> productInCart = prodincrep.findById(prod.getId());
        if (!productInCart.isPresent())
            throw new ProductNotFoundException();
        if(productInCart.get().getQty()<qty)
            throw new InvalidProductException();


        if(foundCart.isPresent()){
            List<ProductInCart> products = foundCart.get().getProducts();

            for(ProductInCart product: products) {
                if (product.equals(productInCart.get()))
                    if (productInCart.get().getQty() == qty) {
                        foundCart.get().getProducts().remove(productInCart.get());
                    } else {
                        product.setQty(product.getQty() - qty);
                    }
                break;
            }
            carep.save(foundCart.get());
            prodincrep.delete(productInCart.get());
            return true;
        }
        return false;
    }


    public Product addProduct(Cart cart, Product product, int qty) {
        if (product == null || product.getPrize() < 0.0)
            throw new InvalidProductException();
        if (cart == null)
            throw new CartNotFoundException();
        Optional<Cart> foundCart = carep.findById(cart.getId());
        if (foundCart.isEmpty())
            throw new CartNotFoundException();
        Optional<Product> foundProduct = prep.findById(product.getId());
        if (foundProduct.isEmpty())
            throw new InvalidProductException();
        if (foundProduct.get().getAvailablePz() < 1)
            throw new ProductOutOfStockException();
        if (foundProduct.get().getAvailablePz() < qty)
            throw new QtyUnavaliableException();

        if (foundCart.get().getProducts() != null) {
            // Controlla se il prodotto è già presente nel carrello
            boolean productFound = false;
            for (ProductInCart p : foundCart.get().getProducts()) {
                if (p.getProduct().getId() == (product.getId())) {
                    // Aggiorna la quantità del prodotto esistente nel carrello
                    p.setQty(p.getQty() + qty);
                    productFound = true;
                    break;
                }
            }
            if (!productFound) {
                // Se il prodotto non è già presente nel carrello, aggiungilo come nuovo prodotto
                ProductInCart pc = new ProductInCart(product);
                pc.setQty(qty);
                foundCart.get().getProducts().add(pc);
            }
        } else {
            // Se il carrello non ha ancora nessun prodotto, aggiungi il prodotto come nuovo
            List<ProductInCart> products = new ArrayList<>();
            ProductInCart pc = new ProductInCart(product);
            pc.setQty(qty);
            products.add(pc);
            foundCart.get().setProducts(products);
        }

        // Aggiorna la quantità totale del carrello
        foundCart.get().setQty(foundCart.get().getProducts().size());
        carep.save(foundCart.get());
        return product;
    }


    @Transactional(readOnly = true)
    public Optional<ProductInCart> showProductsById(long id){
        return prodincrep.findById(id);
    }


    @Transactional(readOnly = true)
    public List<ProductInCart> showAllProductsInCart() {
        return prodincrep.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Order> clientsOrders(Client c){
        if(c == null)
            throw new ClientDoesntExistException();
        return  orderep.findByClient(c);
    }

    @Transactional(readOnly = true)
    public Optional<Order>  clientsOrdersInDate(Client c, Date date){
        if(c == null)
            throw new ClientDoesntExistException();

        return  orderep.findByClientAndCreatDate(c,date);
    }

    @Transactional(readOnly = true)
    public Optional<Order>  clientsOrdersBetweenDates(Client c, Date firstDate, Date lastDate){
        if(c == null)
            throw new ClientDoesntExistException();

        return  orderep.findByBuyerInPeriod(firstDate, lastDate,c);
    }

    @Transactional(readOnly=true)
    public Client getClientFromEmail(String email) throws ClientDoesntExistException
    {
        if(email==null)
            throw new ClientDoesntExistException();
        Optional<Client> client = cr.findByEmail(email.toLowerCase());
        if(client.isPresent())
            return client.get();
        else
            throw new ClientDoesntExistException(email);
    }

    @Transactional(readOnly=true)
    public Cart getCartFromClient(Client client) throws InvalidClientException
    {
        if(client==null)
            throw new InvalidClientException();
        Optional<Cart> cart = carep.findByClient(client);
        if(cart.isPresent())
            return cart.get();
        else
            throw new InvalidClientException();
    }

    @Transactional(readOnly = true)
    public Optional<ProductInCart> getProductFromCart(String name, String color, Cart cart){
        if(name == null){
            throw new InvalidProductException();
        }
        if(cart == null){
            throw new CartNotFoundException();
        }

        Optional<Cart> foundCart = carep.findById(cart.getId());
        if(foundCart.isEmpty()){
            throw new CartNotFoundException();
        }
        for(ProductInCart prodInCart : foundCart.get().getProducts()){
            Optional<ProductInCart> foundProduct = prodincrep.findById(prodInCart.getId());
            if(foundProduct.isPresent()){
                if(foundProduct.get().getProduct().getName().equalsIgnoreCase(name) && foundProduct.get().getProduct().getColor().equals(color))
                    return foundProduct;
            }
        }
        return Optional.empty();
    }

    public boolean substitute(ProductInCart productInCart,int qty, Cart cart) {
        if (cart == null)
            throw new CartNotFoundException();
        if (productInCart == null)
            throw new ProductNotFoundException();

        Optional<Cart> foundCart = carep.findById(cart.getId());
        if (!foundCart.isPresent())
            throw new CartNotFoundException();

        Optional<ProductInCart> productInCartTmp = prodincrep.findById(productInCart.getId());
        if (!productInCartTmp.isPresent())
            throw new ProductNotFoundException();

        Cart existingCart = foundCart.get();
        List<ProductInCart> products = existingCart.getProducts();
        boolean productFound = false;
        for (ProductInCart product : products) {
            if (product.equals(productInCartTmp.get())) {
                product.setQty(qty);
                productFound = true;
                break;
            }
        }
        if (productFound) {
            carep.save(existingCart);
            return true;
        } else {
            return false;
        }
    }


}
