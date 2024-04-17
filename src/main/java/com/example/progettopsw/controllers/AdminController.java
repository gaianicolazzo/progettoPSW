package com.example.progettopsw.controllers;

import com.example.progettopsw.DTO.ProductDTO;
import com.example.progettopsw.exceptions.BrandAlreadyExistsException;
import com.example.progettopsw.exceptions.BrandNotFoundException;
import com.example.progettopsw.exceptions.InvalidProductException;
import com.example.progettopsw.exceptions.ProductAlreadyExistsException;
import com.example.progettopsw.modules.Brand;
import com.example.progettopsw.modules.Product;
import com.example.progettopsw.repositories.BrandRepository;
import com.example.progettopsw.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping(value= "/admin")
public class AdminController {

    @Autowired
    ProductService productService;



    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value="product")
    public ResponseEntity<String> putProduct(@RequestBody ProductDTO product)
    {
        {
            Product ret = new Product();
            Brand brand = productService.getBrandByName(product.getBrand().toLowerCase()).get();
            try{
                ret.setAvailablePz(product.getAvailablePz());
                ret.setPrize(product.getPrize());
                ret.setName(product.getName());
                ret.setBrand(brand);
                ret.setBarCode(product.getBarCode());
                ret.setCreatData(product.getCreatData());
                ret.setDescr(product.getDescr());
                ret.setColor(product.getColor());
                ret.setCategory(product.getCategory());
                ret = productService.addProduct(ret);
            }catch(BrandNotFoundException bnfe)
            {
                return new ResponseEntity<>("Brand not found.", HttpStatus.NOT_FOUND);
            }catch(ProductAlreadyExistsException paee){
                return new ResponseEntity<>("Prodotto already exists.", HttpStatus.NOT_ACCEPTABLE);
            }catch(InvalidProductException ipe){
                return new ResponseEntity<>("Quantity or Price out of bound.", HttpStatus.NOT_ACCEPTABLE);
            }catch(OptimisticLockingFailureException olfe)
            {
                return new ResponseEntity<>("L'operazione non è andata a buon fine", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(ret.toString(), HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping(value="brand")
    public ResponseEntity<String> putBrand(@RequestBody String nomeBrand) {
        Brand ret = null;
        try{
            Brand tmp=new Brand(nomeBrand);
            ret = productService.loadBrand(tmp);
        }
        catch(BrandAlreadyExistsException baee){
            return new ResponseEntity<>("Brand already exists.", HttpStatus.NOT_ACCEPTABLE);
        }catch(OptimisticLockingFailureException olfe)
        {
            return new ResponseEntity<>("L'operazione non è andata a buon fine", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ret.getName(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public ResponseEntity<Product> getProductsByBarCode(
            @RequestParam(value = "barCode",required = false) String barCode){
        Optional<Product> ret = productService.showProductsByBarCode(barCode);
        if(ret.isPresent())
            return new ResponseEntity<>(ret.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.OK);
    }



}
