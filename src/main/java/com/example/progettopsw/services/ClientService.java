package com.example.progettopsw.services;

import com.example.progettopsw.exceptions.*;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

            int cartQty = foundCart.get().getQta();

            for(ProductInCart product: products) {
                if (product.equals(productInCart.get()))
                    if (product.getQty() == qty) {
                        foundCart.get().getProducts().remove(productInCart.get());
                        foundCart.get().setQta(cartQty--);
                        prodincrep.delete(productInCart.get());
                    } else {
                        product.setQty(product.getQty() - qty);
                        prodincrep.save(product);
                    }
                break;
            }
            carep.save(foundCart.get());
            return true;
        }
        return false;
    }


    public Product addProduct(Cart cart, Product product, int qty){
        if(product == null || product.getPrize()<0.0)
            throw new InvalidProductException();
        if(cart == null)
            throw new CartNotFoundException();
        Optional<Cart> foundCart=carep.findById(cart.getId());

        if(foundCart.isEmpty())
            throw new CartNotFoundException();
        Optional<Product> foundProduct= prep.findById(product.getId());
        if(foundProduct.isEmpty())
            throw new InvalidProductException();

        if(foundProduct.get().getAvailablePz()<1)
            throw new ProductOutOfStockException();
        if(foundProduct.get().getAvailablePz()<qty)
            throw new QtyUnavaliableException();

        ProductInCart pc = new ProductInCart(foundProduct.get());
        pc.setQty(qty);

        int qtyCart = cart.getQta();

        if(foundCart.get().getProducts() == null){
            foundCart.get().setProducts(new LinkedList<>());
        }else if(foundCart.get().getProducts().contains(pc)){
            throw new ProductAlreadyInCartException();
        }

        cart.getProducts().add(pc);
        cart.setQta(qtyCart++);

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

}
