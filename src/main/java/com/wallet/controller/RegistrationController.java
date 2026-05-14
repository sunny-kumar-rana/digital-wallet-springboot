package com.wallet.controller;

import com.wallet.service.UserService;

import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.dto.RegisterRequestDto;


@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequestDto dto) {

        try{

            userService.register(dto);

            return "User Registered Successfully";

        } catch (Exception e){
            return e.getMessage();
        }
    }
}
