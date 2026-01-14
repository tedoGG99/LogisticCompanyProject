package com.company.logistics.service.impl;

import com.company.logistics.entity.Client;
import com.company.logistics.repository.ClientRepository;
import com.company.logistics.service.ClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Optional<Client> findById(Integer userId) {
        return clientRepository.findById(userId);
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
