package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long > {

    boolean existsByName(String nome);

    Brand findByName(String nome);
}
