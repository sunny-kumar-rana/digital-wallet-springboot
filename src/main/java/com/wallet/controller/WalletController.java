package com.wallet.controller;


import com.wallet.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class WalletController{

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance(@RequestParam long userId) {

        return Map.of(
                "userId", userId,
                "balance", walletService.getBalance(userId)
        );

    }

    @PostMapping("/deposit")
    public Map<String, String> deposit(
            @RequestParam long userId,
            @RequestParam double amount
    ) {

        walletService.deposit(
                userId,
                BigDecimal.valueOf(amount)
        );

        return Map.of(
                "message",
                "Deposit Successful"
        );
    }
}
