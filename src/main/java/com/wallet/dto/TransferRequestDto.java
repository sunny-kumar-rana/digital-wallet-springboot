package com.wallet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferRequestDto {

    @Positive(message = "Sender ID must be positive")
    private long senderId;

    @Positive(message = "Receiver ID must be positive")
    private long receiverId;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than zero"
    )
    private BigDecimal amount;

    public TransferRequestDto() {
    }

    public TransferRequestDto(
            long senderId,
            long receiverId,
            BigDecimal amount
    ) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}