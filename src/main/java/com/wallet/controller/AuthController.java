package com.wallet.controller;

import com.wallet.dto.LoginRequestDto;
import com.wallet.model.User;
import com.wallet.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto dto) {

        try{

            User user = userService.login(dto);

            return "Login Successful : " + user.getName();

        } catch (Exception e){
            return e.getMessage();
        }
    }
}
