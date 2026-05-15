package com.wallet.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "sender_id", nullable = false)
    private long senderId;

    @Column(name = "receiver_id", nullable = false)
    private long receiverId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at")
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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
