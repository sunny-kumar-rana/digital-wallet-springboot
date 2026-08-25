package com.wallet.controller;

import com.wallet.model.Transaction;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TransactionController {

    private final WalletService walletService;

    @Autowired
    public TransactionController(
            WalletService walletService
    ) {
        this.walletService = walletService;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {

        long userId =
                AuthenticatedUser.getUserId();

        return walletService.getTransactionHistory(
                userId
        );
    }
}