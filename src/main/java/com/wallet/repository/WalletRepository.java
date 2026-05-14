package com.wallet.repository;

import com.wallet.model.Wallet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class WalletRepository {
    public Wallet findByUserId(Connection conn, long userId) throws Exception{
        String query = "SELECT user_id, balance FROM wallets WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,userId);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return new Wallet(rs.getLong("user_id"),rs.getBigDecimal("balance"));
        }
        return null;
    }


    public Wallet findByUserIdForUpdate(Connection conn, long userId) throws Exception{
        String query = "SELECT user_id, balance FROM wallets WHERE user_id = ? FOR UPDATE";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,userId);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return new Wallet(rs.getLong("user_id"),rs.getBigDecimal("balance"));
        }
        return null;
    }

    public void updateBalance(Connection conn, long userId, BigDecimal amount) throws SQLException {
        String query = "UPDATE wallets SET balance = ? WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setBigDecimal(1,amount);
        ps.setLong(2,userId);
        ps.executeUpdate();
    }

    public void createWallet(Connection conn, long userId) throws SQLException{
        String query = "INSERT INTO wallets (user_id, balance, updated_at) VALUES (?, 0, CURRENT_TIMESTAMP)";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,userId);

        ps.executeUpdate();
    }
    public BigDecimal getBalance(Connection conn, long userId) throws SQLException {
        String query = "SELECT balance FROM wallets WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,userId);

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getBigDecimal("balance");
        }
        return null;
    }
}
