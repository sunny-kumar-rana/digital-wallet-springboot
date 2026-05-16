package com.wallet.service;

import com.wallet.exception.InsufficientBalanceException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             TransactionRepository transactionRepository) {

        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void transfer(long senderId, long receiverId, BigDecimal amount) {

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if(senderId == receiverId){
            throw new IllegalArgumentException("Sender and receiver cannot be same");
        }

        Wallet sender = walletRepository.findById(senderId)
                .orElseThrow(() ->
                        new WalletNotFoundException("Sender wallet not found"));

        Wallet receiver = walletRepository.findById(receiverId)
                .orElseThrow(() ->
                        new WalletNotFoundException("Receiver wallet not found"));

        if(sender.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        walletRepository.save(sender);
        walletRepository.save(receiver);

        Transaction transaction = new Transaction();

        transaction.setSenderId(senderId);
        transaction.setReceiverId(receiverId);
        transaction.setAmount(amount);
        transaction.setStatus("SUCCESS");

        transactionRepository.save(transaction);
    }

    @Override
    public BigDecimal getBalance(long userId) {

        Wallet wallet = walletRepository.findById(userId)
                .orElseThrow(() ->
                        new WalletNotFoundException("Wallet not found"));

        return wallet.getBalance();
    }

    @Override
    public List<Transaction> getTransactionHistory(long userId) {

        return transactionRepository
                .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(
                        userId,
                        userId
                );
    }
}