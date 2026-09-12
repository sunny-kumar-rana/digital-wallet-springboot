package com.wallet.dto;

import java.math.BigDecimal;

public class BalanceResponseDto {

    private long userId;
    private BigDecimal balance;

    public BalanceResponseDto() {
    }

    public BalanceResponseDto(long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}