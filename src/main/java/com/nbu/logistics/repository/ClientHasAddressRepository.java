package com.nbu.logistics.repository;

import com.company.logistics.entity.ClientHasAddress;
import com.company.logistics.entity.ClientAddressId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientHasAddressRepository
        extends JpaRepository<ClientHasAddress, ClientAddressId> {

    List<ClientHasAddress> findByIdClientUserId(Integer clientUserId);
}