package com.wallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private long id;
    private long senderId;
    private long receiverId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    public Transaction(){}
    public Transaction(long senderId, long receiverId, BigDecimal amount, String status){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = status;
    }

    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public long getSenderId(){
        return this.senderId;
    }
    public void setSenderId(long senderId){
        this.senderId = senderId;
    }
    public long getReceiverId(){
        return this.receiverId;
    }
    public void setReceiverId(long receiverId){
        this.receiverId = receiverId;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public LocalDateTime getCreatedAt(){
        return this.createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
}
