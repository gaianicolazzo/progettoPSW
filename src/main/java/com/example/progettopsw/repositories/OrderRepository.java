package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Client;

import com.example.progettopsw.modules.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByClient(Client client);

    Optional<Order> findByClientAndDate(Client client, Date date);


    @Query("select o from Order o where o.date > ?1 and o.date < ?2 and o.client = ?3")
    Optional<Order> findByBuyerInPeriod(Date startDate, Date endDate, Client client);
}
