package com.example.progettopsw.services;

import com.example.progettopsw.DTO.ProductInCartDTO;
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
public class OrderService {

    @Autowired
    private ClientRepository clientR;

    @Autowired
    private ClientOrderRepository ordrep;

    @Autowired
    private AddedProductsRepository addprep;

    @Autowired
    private ProductRepository prep;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    ProductInCartRepository pricrep;

    @Autowired
    ClientService cserv;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = {QtyUnavaliableException.class, PrizeChangedException.class})
    public boolean crea(Cart cart, List<ProductInCart> items, Client client) throws QtyUnavaliableException, PrizeChangedException, ProductOutOfStockException,InvalidClientException, CartNotFoundException{
        if(items.isEmpty())
            return true;
        if(cart==null)
            throw new CartNotFoundException();
        Optional<Cart> cartManaged = cartRepository.findById(cart.getId());
        if(!cartManaged.isPresent())
            throw new CartNotFoundException();

        Order o = new Order();
        Optional<Client> clientManaged = clientR.findById(client.getId());
        if(!clientManaged.isPresent()){
            throw new InvalidClientException();
        }
        o.setClient(clientManaged.get());
        o.setCreatDate(new Date(System.currentTimeMillis()));
        ordrep.save(o);
        for(ProductInCart itd:items){
            Optional<Product> op = prep.findProductByName(itd.getProduct().getName());
            if(!op.isPresent()) throw new ProductOutOfStockException();
            Product p = op.get();
            if(p.getPrize() != itd.getPrize())
                throw new PrizeChangedException("Il prezzo del prodotto è cambiato nel prezzo " + p.getPrize());
            if(p.getAvailablePz() < itd.getQty())
                throw new QtyUnavaliableException(p.getName());
            OrderDetail dto = new OrderDetail();
            dto.setProduct(p);
            dto.setPrize(itd.getPrize());
            dto.setQty(itd.getQty());
            addprep.save(dto);
            o.getDetails().add(dto);
            p.setAvailablePz(p.getAvailablePz()- itd.getQty());
            cleanCart(cart);
        }
        return true;
    }
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void cleanCart(Cart cart)
            throws CartNotFoundException, ProductNotFoundException
    {
        if(cart == null)
            throw new CartNotFoundException();
        Optional<Cart> foundCart = cartRepository.findById(cart.getId());
        if(! foundCart.isPresent())
            throw new CartNotFoundException();

        LinkedList<ProductInCart> supportList = new LinkedList<>(foundCart.get().getProducts());

        for(ProductInCart cartProduct : supportList)
        {
            Optional<ProductInCart> foundProduct = pricrep.findById(cartProduct.getId());
            if(!foundProduct.isPresent())
                throw new ProductNotFoundException();
            foundCart.get().getProducts().remove((foundProduct.get()));
        }
        foundCart.get().setQty(0);
        cartRepository.save(foundCart.get());
        pricrep.deleteAll(supportList);

    }

    @Transactional(readOnly = true)
    public List<Order> getOrdini(String email){
        Client c = clientR.findByEmail(email).get();
        return c.getOrders();

    }
}
