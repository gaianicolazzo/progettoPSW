package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddedProductsRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByProductsPrizeGreaterThanEqualAndProductsPrizeLessThan(float productsPrize, float productsPrize2);


}
