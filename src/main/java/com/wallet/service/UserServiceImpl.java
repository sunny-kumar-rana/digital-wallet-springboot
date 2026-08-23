package com.wallet.service;

import com.wallet.dto.LoginRequestDto;
import com.wallet.dto.RegisterRequestDto;
import com.wallet.exception.InvalidCredentialsException;
import com.wallet.exception.UserNotFoundException;
import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.security.CustomUserPrincipal;
import com.wallet.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(
            UserRepository userRepository,
            WalletRepository walletRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public void register(RegisterRequestDto dto) {

        validateRegistration(dto);

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        if (userRepository.findByEmail(email) != null) {

            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setName(
                dto.getName().trim()
        );

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        User savedUser =
                userRepository.save(user);

        Wallet wallet = new Wallet();

        wallet.setUserId(
                savedUser.getId()
        );

        wallet.setBalance(
                BigDecimal.ZERO
        );

        walletRepository.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public User login(LoginRequestDto dto) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Login request cannot be null"
            );
        }

        String email =
                dto.getEmail()
                        .trim()
                        .toLowerCase();

        User user =
                userRepository.findByEmail(email);

        if (user == null) {

            throw new UserNotFoundException(
                    "User not found"
            );
        }

        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Invalid credentials"
            );
        }

        return user;
    }

    public String generateToken(User user) {

        CustomUserPrincipal principal =
                new CustomUserPrincipal(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword()
                );

        return jwtService.generateToken(
                principal
        );
    }

    private void validateRegistration(
            RegisterRequestDto dto
    ) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Registration request cannot be null"
            );
        }

        validateName(dto.getName());
        validateEmail(dto.getEmail());
        validatePassword(dto.getPassword());
    }

    private void validateName(String name) {

        if (name == null ||
                name.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Name cannot be empty"
            );
        }
    }

    private void validateEmail(String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty"
            );
        }
    }

    private void validatePassword(String password) {

        if (password == null ||
                password.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Password cannot be empty"
            );
        }

        if (password.length() < 4) {

            throw new IllegalArgumentException(
                    "Password must be at least 4 characters"
            );
        }
    }
}