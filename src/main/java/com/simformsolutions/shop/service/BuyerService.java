package com.simformsolutions.shop.service;

import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.RoleRepository;
import com.simformsolutions.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public User saveBuyer(User user) {
        Role role = roleRepository.findByName("buyer");
        user.setRole(role);
        return userRepository.save(user);

    }
}
