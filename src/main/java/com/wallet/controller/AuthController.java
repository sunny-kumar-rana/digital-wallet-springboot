package com.wallet.controller;

import com.wallet.dto.LoginRequestDto;
import com.wallet.model.User;
import com.wallet.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequestDto dto) {

        User user = userService.login(dto);

        return Map.of(
                "message", "Login Successful",
                "userId", user.getId(),
                "name", user.getName()
        );

    }
}
