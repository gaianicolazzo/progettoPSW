package com.example.progettopsw.repositories;

import com.example.progettopsw.modules.Cart;
import com.example.progettopsw.modules.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByFirstName(String firstName);
    List<Client> findByLastName(String lastName);
    List<Client> findByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String email);


}
