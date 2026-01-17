package com.company.logistics.repository;

import com.company.logistics.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    boolean existsByUserId(Integer userId);
}//