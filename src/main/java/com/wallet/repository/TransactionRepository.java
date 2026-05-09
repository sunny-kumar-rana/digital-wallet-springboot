package com.wallet.repository;

import com.wallet.model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void createTransaction(Connection conn, long senderId, long receiverId, BigDecimal amount, String status) throws SQLException {
        String query = "INSERT INTO transactions(id, sender_id, receiver_id, amount, status) VALUES (transaction_seq.NEXTVAL,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,senderId);
        ps.setLong(2,receiverId);
        ps.setBigDecimal(3,amount);
        ps.setString(4,status);

        ps.executeUpdate();
    }

    public List<Transaction> findByUser(Connection conn, long userId) throws SQLException {
        String query = """
                SELECT id, sender_id, receiver_id, amount, status, created_at 
                FROM transactions 
                WHERE sender_id = ? OR receiver_id = ? 
                ORDER BY created_at DESC""";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1,userId);
        ps.setLong(2,userId);

        ResultSet rs = ps.executeQuery();

        List<Transaction> transactions = new ArrayList<>();

        while (rs.next()){
            Transaction tsx = new Transaction();

            tsx.setId(rs.getLong("id"));
            tsx.setSenderId(rs.getLong("sender_id"));
            tsx.setReceiverId(rs.getLong("receiver_id"));
            tsx.setAmount(rs.getBigDecimal("amount"));
            tsx.setStatus(rs.getString("status"));

            transactions.add(tsx);
        }
        return transactions;
    }
}
