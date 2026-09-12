package com.wallet.controller;

import com.wallet.dto.TransferRequestDto;
import com.wallet.dto.TransferResponseDto;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransferController {

    private final WalletService walletService;

    public TransferController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    public TransferResponseDto transfer(
            @Valid @RequestBody TransferRequestDto dto,
            @RequestHeader(
                    value = "Idempotency-Key",
                    required = false
            )
            String idempotencyKey
    ) {

        long senderId = AuthenticatedUser.getUserId();

        boolean alreadyProcessed =
                walletService.transfer(
                        senderId,
                        dto.getReceiverId(),
                        dto.getAmount(),
                        idempotencyKey
                );

        return new TransferResponseDto(
                alreadyProcessed
                        ? "Transfer already processed"
                        : "Transfer Successful",
                alreadyProcessed
        );
    }
}