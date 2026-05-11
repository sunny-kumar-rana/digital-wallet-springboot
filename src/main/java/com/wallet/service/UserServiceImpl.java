package com.wallet.service;

import com.wallet.dto.LoginRequestDto;
import com.wallet.dto.RegisterRequestDto;
import com.wallet.exception.InvalidCredentialsException;
import com.wallet.exception.UserNotFoundException;
import com.wallet.repository.UserRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.model.User;
import com.wallet.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class UserServiceImpl implements UserService{
    private UserRepository userRepository = new UserRepository();
    private WalletRepository walletRepository = new WalletRepository();

    public void register(RegisterRequestDto dto) throws SQLException, ClassNotFoundException {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        Connection conn = DBConnection.getConnection();

        try{
            conn.setAutoCommit(false);

            long userId = userRepository.createUser(conn, user);
            walletRepository.createWallet(conn, userId);
            conn.commit();
        } catch (Exception e){
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public User login(LoginRequestDto dto) throws SQLException, ClassNotFoundException {

        try (Connection conn = DBConnection.getConnection()) {
            User user = userRepository.findUserByEmail(conn, dto.getEmail());

            if (user == null) {
                throw new UserNotFoundException("User not found");
            }
            if (!user.getPassword().equals(dto.getPassword())) {
                throw new InvalidCredentialsException("Invalid password");
            }

            return user;
        }
    }
}
