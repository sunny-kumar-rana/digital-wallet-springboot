package com.wallet.security;

import com.wallet.model.User;
import com.wallet.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(
                email.trim().toLowerCase()
        );

        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found"
            );
        }

        return new CustomUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword()
        );
    }
}