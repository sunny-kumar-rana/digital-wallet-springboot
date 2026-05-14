package com.wallet.controller;

import com.wallet.model.Transaction;
import com.wallet.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TransactionController {

    private WalletService walletService;

    @Autowired
    public TransactionController(WalletService walletService){
        this.walletService = walletService;
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam long userId) {

        try{

            return walletService.getTransactionHistory(userId);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
