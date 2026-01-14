package com.company.logistics.service;

import com.company.logistics.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User create(User user);
}
