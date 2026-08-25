package com.wallet.controller;

import com.wallet.dto.LoginRequestDto;
import com.wallet.model.User;
import com.wallet.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @Valid @RequestBody LoginRequestDto dto
    ) {

        User user =
                userService.login(dto);

        String token =
                userService.generateToken(user);

        return Map.of(
                "message", "Login Successful",
                "token", token,
                "userId", user.getId(),
                "name", user.getName()
        );
    }
}