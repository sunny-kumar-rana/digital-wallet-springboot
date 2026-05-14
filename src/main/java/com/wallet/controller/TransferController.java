package com.wallet.controller;

import com.wallet.dto.TransferRequestDto;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransferController {

    private final WalletService walletService;

    @Autowired
    public TransferController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequestDto dto) {

        try{

            walletService.transfer(
                    dto.getSenderId(),
                    dto.getReceiverId(),
                    dto.getAmount()
            );

            return "Transfer Successful";

        } catch (Exception e){
            return e.getMessage();
        }
    }
}
