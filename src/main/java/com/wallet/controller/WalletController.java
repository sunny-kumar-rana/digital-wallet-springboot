package com.wallet.controller;


import com.wallet.service.WalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WalletController{

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public String getBalance(@RequestParam long userId) {

        try{
            return "Balance = " + walletService.getBalance(userId);

        } catch (Exception e){
            return e.getMessage();
        }
    }
}
