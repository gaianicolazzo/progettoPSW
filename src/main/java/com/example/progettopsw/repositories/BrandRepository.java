package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand,Long > {

    boolean existsByName(String nome);

    Optional<Brand> findByName(String nome);
}
