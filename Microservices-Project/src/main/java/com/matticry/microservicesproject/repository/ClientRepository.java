package com.matticry.microservicesproject.repository;

import com.matticry.microservicesproject.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository  extends JpaRepository<Client, Long> {
    Optional<Client> findByDni(String dni);
    List<Client> findByStatusClient(Character status);
    Optional<Client> findByName (String name);
}
