package com.wallet.dto;

public class LoginRequestDto {
    private String name;
    private String password;

    public LoginRequestDto(String name, String password){
        this.name = name;
        this.password = password;
    }
}
