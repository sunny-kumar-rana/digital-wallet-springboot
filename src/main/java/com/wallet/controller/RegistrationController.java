package com.wallet.controller;

import com.wallet.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.RegisterRequestDto;

import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequestDto dto) {

        userService.register(dto);

        return Map.of(
                "message", "User Registered Successfully"
        );
    }
}
