package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.UserNotFoundException;
import com.simformsolutions.shop.repository.RoleRepository;
import com.simformsolutions.shop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    SellerService sellerService;

    public User userDetailsToUser(UserDetail userDetail) {
        return modelMapper.map(userDetail, User.class);
    }

    public User saveSuperAdmin(UserDetail userDetail) {
        Role role = roleRepository.findByName("super-admin");
        userDetail.setPassword(new BCryptPasswordEncoder().encode(userDetail.getPassword()));
        User user = userDetailsToUser(userDetail);
        user.setEnable(true);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    public User findSuperAdminById(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new UserNotFoundException(userId + "");
    }

    public User findSuperAdminByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new UserNotFoundException(email);
    }

    public List<User> getAllSellers() {
        Role role = roleRepository.findByName("seller");
        return role.getUsers();
    }

    public User updateUser(User user) {
        User currentUser = findSuperAdminById(user.getUserId());
        currentUser.setName(user.getName());
        currentUser.setPassword(user.getPassword());
        currentUser.setEmail(user.getEmail());
        currentUser.setContact(user.getContact());
        currentUser.setAddress(user.getAddress());
        return userRepository.save(currentUser);
    }

    public void deleteSeller(int userId) {
        Role role = roleRepository.findByName("seller");
        User user = sellerService.findSellerById(userId);
        user.getRoles().remove(role);
        role.getUsers().remove(user);
        roleRepository.save(role);
        userRepository.save(user);
    }
}
