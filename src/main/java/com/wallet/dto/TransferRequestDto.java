package com.wallet.dto;

import java.math.BigDecimal;

public class TransferRequestDto {

    private long senderId;
    private long receiverId;
    private BigDecimal amount;

    public TransferRequestDto() {
    }

    public TransferRequestDto(long senderId, long receiverId, BigDecimal amount) {
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
