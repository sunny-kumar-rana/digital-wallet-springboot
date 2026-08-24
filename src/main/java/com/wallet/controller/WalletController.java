package com.wallet.controller;

import com.wallet.dto.MoneyRequestDto;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(
            WalletService walletService
    ) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {

        long userId =
                AuthenticatedUser.getUserId();

        return Map.of(
                "userId", userId,
                "balance",
                walletService.getBalance(userId)
        );
    }

    @PostMapping("/deposit")
    public Map<String, String> deposit(
            @Valid @RequestBody MoneyRequestDto request
    ) {

        long userId =
                AuthenticatedUser.getUserId();

        walletService.deposit(
                userId,
                request.getAmount()
        );

        return Map.of(
                "message",
                "Deposit Successful"
        );
    }

    @PostMapping("/withdraw")
    public Map<String, String> withdraw(
            @Valid @RequestBody MoneyRequestDto request
    ) {

        long userId =
                AuthenticatedUser.getUserId();

        walletService.withdraw(
                userId,
                request.getAmount()
        );

        return Map.of(
                "message",
                "Withdrawal Successful"
        );
    }
}