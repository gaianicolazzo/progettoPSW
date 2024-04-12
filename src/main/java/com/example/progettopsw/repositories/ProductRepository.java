package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Brand;
import com.example.progettopsw.modules.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
    List<Product> findByPrizeBetween(float prize, float prize2);
    List<Product> findByAvailablePzGreaterThanEqual(int availablePz);
    Optional<Product> findByBarCode(String barCode);
    boolean existsByBarCode(String barCode);
    List<Product> findByBrand(Brand brand);

    Optional<Product> findByName(String name);

    Optional<Product> findProductByNameAndCategoryAndColor(String name, String category, String color);

}
