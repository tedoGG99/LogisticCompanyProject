package com.company.logistics.service;

import com.company.logistics.entity.Client;

import java.util.Optional;

public interface ClientService {

    Optional<Client> findById(Integer userId);

    Client save(Client client);
}
