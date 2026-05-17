package com.wallet.service;

import com.wallet.dto.LoginRequestDto;
import com.wallet.dto.RegisterRequestDto;
import com.wallet.model.User;

public interface UserService {

    void register(RegisterRequestDto dto);

    User login(LoginRequestDto dto);
}