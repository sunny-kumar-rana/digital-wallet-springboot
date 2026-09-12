package com.wallet.dto;

public class TransferResponseDto {

    private String message;
    private boolean alreadyProcessed;

    public TransferResponseDto() {
    }

    public TransferResponseDto(
            String message,
            boolean alreadyProcessed
    ) {
        this.message = message;
        this.alreadyProcessed = alreadyProcessed;
    }

    public String getMessage() {
        return message;
    }

    public boolean isAlreadyProcessed() {
        return alreadyProcessed;
    }
}