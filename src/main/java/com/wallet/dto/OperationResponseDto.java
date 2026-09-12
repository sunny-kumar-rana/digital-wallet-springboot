package com.wallet.dto;

public class OperationResponseDto {

    private String message;

    public OperationResponseDto() {
    }

    public OperationResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}