package com.wallet.model;

import java.math.BigDecimal;

public class Wallet {
    private long userId;
    private BigDecimal balance;

    public Wallet(){}
    public Wallet(long id, BigDecimal balance){
        this.userId = id;
        this.balance = balance;
    }

    public long getUserId(){
        return this.userId;
    }
    public void setUserId(long userId){
        this.userId = userId;
    }
    public BigDecimal getBalance(){
        return this.balance;
    }
    public void setBalance(BigDecimal balance){
        this.balance = balance;
    }
}
