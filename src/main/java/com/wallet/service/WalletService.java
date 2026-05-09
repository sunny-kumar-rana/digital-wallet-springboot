package com.wallet.service;

import com.wallet.model.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface WalletService {
    public void transfer(long senderId, long receiverId, BigDecimal amount) throws SQLException, ClassNotFoundException;
    public BigDecimal getBalance(long userId) throws SQLException, ClassNotFoundException;
    public List<Transaction> getTransactionHistory(long userId) throws ClassNotFoundException, SQLException;
}
