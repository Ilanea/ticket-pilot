package com.mci.ticketpilot.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mci.ticketpilot.data.entity.Users;
import com.mci.ticketpilot.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class PilotUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PilotUserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public PilotUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            String role = user.getUserRole().name();
            logger.info("User logged in: {} (Role: {})", email, role);
            return org.springframework.security.core.userdetails.User
                    .builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found: " + email);
        }
    }
}