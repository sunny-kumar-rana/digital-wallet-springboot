package com.wallet.controller;

import com.wallet.dto.BalanceResponseDto;
import com.wallet.dto.MoneyRequestDto;
import com.wallet.dto.OperationResponseDto;
import com.wallet.security.AuthenticatedUser;
import com.wallet.service.WalletService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public BalanceResponseDto getBalance() {

        long userId = AuthenticatedUser.getUserId();

        return new BalanceResponseDto(
                userId,
                walletService.getBalance(userId)
        );
    }

    @PostMapping("/deposit")
    public OperationResponseDto deposit(
            @Valid @RequestBody MoneyRequestDto request
    ) {

        long userId = AuthenticatedUser.getUserId();

        walletService.deposit(
                userId,
                request.getAmount()
        );

        return new OperationResponseDto(
                "Deposit Successful"
        );
    }

    @PostMapping("/withdraw")
    public OperationResponseDto withdraw(
            @Valid @RequestBody MoneyRequestDto request
    ) {

        long userId = AuthenticatedUser.getUserId();

        walletService.withdraw(
                userId,
                request.getAmount()
        );

        return new OperationResponseDto(
                "Withdrawal Successful"
        );
    }
}