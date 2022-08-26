package com.simformsolutions.shop.service;

import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("");

        List<String> roles = user.get().getRoles().stream().map(Role::getName).toList();
        String path = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        String userRole = Arrays.stream(path.split("/")).toList().get(4);

        if (roles.contains(userRole))
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), Arrays.asList(new SimpleGrantedAuthority(userRole)));
        throw new UsernameNotFoundException("Not found: " + username);
    }
}
