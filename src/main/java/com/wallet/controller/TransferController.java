package com.wallet.controller;

import com.wallet.dto.TransferRequestDto;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class TransferController {

    private final WalletService walletService;

    @Autowired
    public TransferController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    public Map<String, String> transfer(@RequestBody TransferRequestDto dto) {

        try{

            walletService.transfer(
                    dto.getSenderId(),
                    dto.getReceiverId(),
                    dto.getAmount()
            );

            return Map.of(
                    "message", "Transfer Successful"
            );

        } catch (Exception e){
            return Map.of(
                    "error", e.getMessage()
            );
        }
    }
}
