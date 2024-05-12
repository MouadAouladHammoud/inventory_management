package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
