package com.wallet.controller;

import com.wallet.dto.TransactionResponseDto;
import com.wallet.model.Transaction;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    private final WalletService walletService;

    public TransactionController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/transactions")
    public List<TransactionResponseDto> getTransactions() {

        long userId = AuthenticatedUser.getUserId();

        return walletService
                .getTransactionHistory(userId)
                .stream()
                .map(TransactionResponseDto::from)
                .toList();
    }
}