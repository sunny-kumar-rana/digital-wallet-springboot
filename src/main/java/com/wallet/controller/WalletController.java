package com.wallet.controller;


import com.wallet.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
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
}
