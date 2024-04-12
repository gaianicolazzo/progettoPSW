package com.example.progettopsw.controllers;


import com.example.progettopsw.DTO.ProductDTO;
import com.example.progettopsw.modules.Product;
import com.example.progettopsw.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("*")
@RequestMapping(value= "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/test")
    //@PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<Product>> getProductTest(){
        List<Product> a = productService.showAllProducts();
        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(value= "pageNumber", defaultValue="0") int pageNumber,
                                                           @RequestParam(value= "pageSize", defaultValue="10") int pageSize,
                                                           @RequestParam(value= "sortBy", defaultValue="id") String sortBy){
        List<Product> res = productService.showAllProducts(pageNumber,pageSize,sortBy);
        List<ProductDTO> ret =new LinkedList<>();
        for(Product prod : res)
            ret.add(new ProductDTO(prod));

        if(res.size()<=0){
            return new ResponseEntity<>(ret, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") long id){
        Optional<Product> ret = productService.showProductsById(id);
        if(ret.isPresent())
            return new ResponseEntity<>(new ProductDTO(ret.get()), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.OK);
    }


    // Endpoint to get products based on characteristics
    @GetMapping("/filtered")
    public ResponseEntity<List<ProductDTO>> getProductsByCharacteristics(
            @RequestParam(value = "category",required = false) String category,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "color",required = false) String color,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Optional<Product> filteredProducts = productService.showProductsByNameandByCategoryandByColor(name,category,color);


        // Paginate the results based on page and pageSize
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, filteredProducts.stream().toList().size());
        List<Product> paginatedProducts = filteredProducts.stream().toList().subList(startIndex, endIndex);

        List<ProductDTO> ret =new LinkedList<>();

        for(Product prod : paginatedProducts)
            ret.add(new ProductDTO(prod));

        // Return the paginated list with a 200 OK status
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    // Endpoint to get products based on characteristics


}