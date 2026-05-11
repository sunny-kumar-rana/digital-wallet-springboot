package com.wallet.service;

import com.wallet.dto.LoginRequestDto;
import com.wallet.dto.RegisterRequestDto;
import com.wallet.model.User;

import java.sql.SQLException;

public interface UserService {
    public void register(RegisterRequestDto dto) throws SQLException, ClassNotFoundException;
    public User login(LoginRequestDto dto) throws SQLException, ClassNotFoundException;
}
