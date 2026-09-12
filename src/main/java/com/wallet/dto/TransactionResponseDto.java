package com.wallet.dto;

import com.wallet.model.Transaction;
import com.wallet.model.TransactionStatus;
import com.wallet.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDto {

    private long id;
    private long senderId;
    private long receiverId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public TransactionResponseDto() {
    }

    public TransactionResponseDto(
            long id,
            long senderId,
            long receiverId,
            BigDecimal amount,
            TransactionType transactionType,
            TransactionStatus status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static TransactionResponseDto from(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getSenderId(),
                transaction.getReceiverId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }

    public long getId() {
        return id;
    }

    public long getSenderId() {
        return senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}