package com.wallet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public Wallet() {
    }

    public Wallet(long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "userId=" + userId +
                ", balance=" + balance +
                '}';
    }
}