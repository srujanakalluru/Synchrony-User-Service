package com.synchrony.cache;


import com.synchrony.entity.User;
import com.synchrony.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UsersCache {

    UserRepository userRepository;

    @Autowired
    public UsersCache(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "usersCache", key = "#name")
    public Optional<User> findByUsername(String name) {
        log.debug("Retrieving from Database for name: " + name);
        return userRepository.findByUsername(name);
    }
}