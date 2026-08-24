package com.wallet.controller;

import com.wallet.dto.TransferRequestDto;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransferController {

    private final WalletService walletService;

    @Autowired
    public TransferController(
            WalletService walletService
    ) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    public Map<String, String> transfer(
            @Valid @RequestBody TransferRequestDto dto
    ) {

        long senderId =
                AuthenticatedUser.getUserId();

        walletService.transfer(
                senderId,
                dto.getReceiverId(),
                dto.getAmount()
        );

        return Map.of(
                "message",
                "Transfer Successful"
        );
    }
}