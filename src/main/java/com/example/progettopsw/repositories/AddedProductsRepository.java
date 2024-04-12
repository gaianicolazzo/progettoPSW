package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddedProductsRepository extends JpaRepository<OrderDetail, Long> {

}
