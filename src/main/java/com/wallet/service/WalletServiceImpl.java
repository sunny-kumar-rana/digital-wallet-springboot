package com.wallet.service;

import com.wallet.exception.InsufficientBalanceException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    public WalletServiceImpl(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository) {

        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void transfer(long senderId, long receiverId, BigDecimal amount) {

        validateAmount(amount);
        validateDifferentWallets(senderId, receiverId);

        Wallet sender = getWallet(senderId);
        Wallet receiver = getWallet(receiverId);

        validateSufficientBalance(sender, amount);

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        saveTransaction(
                senderId,
                receiverId,
                amount,
                "TRANSFER"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(long userId) {
        return getWallet(userId).getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> getTransactionHistory(long userId) {
        return transactionRepository
                .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
    }

    @Override
    @Transactional
    public void deposit(long userId, BigDecimal amount) {

        validateAmount(amount);

        Wallet wallet = getWallet(userId);

        wallet.setBalance(wallet.getBalance().add(amount));

        saveTransaction(
                userId,
                userId,
                amount,
                "DEPOSIT"
        );
    }

    @Override
    @Transactional
    public void withdraw(long userId, BigDecimal amount) {

        validateAmount(amount);

        Wallet wallet = getWallet(userId);

        validateSufficientBalance(wallet, amount);

        wallet.setBalance(wallet.getBalance().subtract(amount));

        saveTransaction(
                userId,
                userId,
                amount,
                "WITHDRAW"
        );
    }

    // -------------------------
    // Helper methods
    // -------------------------

    private Wallet getWallet(long userId) {
        return walletRepository.findById(userId)
                .orElseThrow(() ->
                        new WalletNotFoundException("Wallet not found"));
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }

    private void validateDifferentWallets(
            long senderId,
            long receiverId) {

        if (senderId == receiverId) {
            throw new IllegalArgumentException(
                    "Sender and receiver cannot be same"
            );
        }
    }

    private void validateSufficientBalance(
            Wallet wallet,
            BigDecimal amount) {

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }
    }

    private void saveTransaction(
            long senderId,
            long receiverId,
            BigDecimal amount,
            String transactionType) {

        Transaction transaction = new Transaction();

        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setStatus("SUCCESS");

        transactionRepository.save(transaction);
    }
}