package com.wallet.service;

import com.wallet.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    void transfer(long senderId, long receiverId, BigDecimal amount);

    BigDecimal getBalance(long userId);

    List<Transaction> getTransactionHistory(long userId);
}