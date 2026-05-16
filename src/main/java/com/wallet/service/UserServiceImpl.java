package com.wallet.service;

import com.wallet.dto.LoginRequestDto;
import com.wallet.dto.RegisterRequestDto;
import com.wallet.exception.InvalidCredentialsException;
import com.wallet.exception.UserNotFoundException;
import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           WalletRepository walletRepository) {

        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    public void register(RegisterRequestDto dto) {

        if(dto.getName() == null || dto.getName().trim().isEmpty()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if(dto.getEmail() == null || dto.getEmail().trim().isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if(!dto.getEmail().contains("@")){
            throw new IllegalArgumentException("Invalid email format");
        }

        if(dto.getPassword() == null || dto.getPassword().trim().isEmpty()){
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if(dto.getPassword().length() < 4){
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }

        User existingUser = userRepository.findByEmail(dto.getEmail());

        if(existingUser != null){
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        User savedUser = userRepository.save(user);

        Wallet wallet = new Wallet();

        wallet.setUserId(savedUser.getId());
        wallet.setBalance(BigDecimal.ZERO);

        walletRepository.save(wallet);
    }

    @Override
    public User login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if(user == null){
            throw new UserNotFoundException("User not found");
        }

        if(!user.getPassword().equals(dto.getPassword())){
            throw new InvalidCredentialsException("Invalid password");
        }

        return user;
    }
}