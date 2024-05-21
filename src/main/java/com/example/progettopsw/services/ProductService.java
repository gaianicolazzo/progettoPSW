package com.example.progettopsw.services;

import com.example.progettopsw.exceptions.*;
import com.example.progettopsw.modules.*;
import com.example.progettopsw.repositories.BrandRepository;
import com.example.progettopsw.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy){
        Pageable paging = PageRequest.of(pageNumber,pageSize,Sort.by(sortBy));
        Page<Product> products = productRepository.findAll(paging);
        if (products.hasContent())
            return products.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Product> showProductsByAvailablePieces(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber,pageSize,Sort.by(sortBy));
        Page<Product> products = productRepository.findByAvailablePzGreaterThan(0, paging);;
        if (products.hasContent())
            return products.getContent();
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts() {
        return productRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Product> showProductsByPrice( float priceMin, float priceMax){
        return productRepository.findByPrizeBetween(priceMin, priceMax);
    }

    @Transactional(readOnly = false)
    public Product addProduct(Product p) throws BarCodeAlrExistException {
        Brand brand = p.getBrand();
        if(brand == null || ! brandRepository.existsByName(brand.getName().toLowerCase()))
            throw new BrandNotFoundException();
        brand = brandRepository.findByName(brand.getName().toLowerCase()).get();
        if(p.getBarCode() != null && productRepository.existsByBarCode(p.getBarCode()))
            throw new BarCodeAlrExistException();
        if(p.getAvailablePz() < 1 || p.getPrize()<=0.0)
            throw new InvalidProductException();
        Product product = new Product(p);
        product.setBrand(brand);

        brand.getProducts().add(p);
        return productRepository.save(p);
    }


    @Transactional(readOnly = true)
    public List<Product> showProductsByCategory(String category){
        return productRepository.findProductByCategory(category);
    }

    @Transactional(readOnly = true)
    public Optional<Product> showProductsById(long id){
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Product> showProductsByBarCode(String barCode){
        return productRepository.findByBarCode(barCode);
    }

    @Transactional(readOnly = true)
    public Optional<Brand> getBrandByName(String name){
        return brandRepository.findByName(name);
    }


    public void update(Long id, Product product){
        Optional<Product> foundProduct= productRepository.findById(id);
        if(!foundProduct.isPresent()) throw new ProductDoesntExistException();
        foundProduct.get().setAvailablePz(product.getAvailablePz());
    }


    public boolean delete(Long id){
        Optional<Product> foundProduct= productRepository.findById(id);
        if(!foundProduct.isPresent()) return false;
        productRepository.delete(foundProduct.get());
        return true;
    }

    public Brand loadBrand(Brand brand) throws BrandNotSupportedException, BrandAlreadyExistsException
    {
        if(brand.getProducts()!=null)   //vogliamo bloccare l'aggiunta di un brand che abbia dei prodotti correlati ad esso, prima si aggiunge il brand e poi a parte i prodotti
            if(! brand.getProducts().isEmpty())
                throw new BrandNotSupportedException();
        if(brandRepository.existsByName(brand.getName().toLowerCase()))
            throw new BrandAlreadyExistsException();

        Brand brandToLowerCase=new Brand(brand);

        return brandRepository.save(brandToLowerCase);
    }



    public Optional<Product> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }
}
