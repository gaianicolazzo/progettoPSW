package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Product;
import com.example.progettopsw.modules.ProductInCart;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInCartRepository extends JpaRepository<ProductInCart, Long> {


}
