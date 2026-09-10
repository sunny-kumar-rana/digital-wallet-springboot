package com.wallet.service;

import com.wallet.model.Wallet;
import com.wallet.repository.IdempotencyRecordRepository;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private IdempotencyRecordRepository idempotencyRecordRepository;

    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(
                walletRepository,
                transactionRepository,
                idempotencyRecordRepository
        );
    }

    @Test
    void deposit_shouldIncreaseBalanceAndCreateTransaction() {

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(wallet));

        walletService.deposit(
                1L,
                new BigDecimal("50.00")
        );

        assertEquals(
                new BigDecimal("150.00"),
                wallet.getBalance()
        );

        verify(walletRepository)
                .findByIdForUpdate(1L);

        verify(transactionRepository)
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void withdraw_shouldRejectWhenBalanceIsInsufficient() {

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(wallet));

        org.junit.jupiter.api.Assertions.assertThrows(
                com.wallet.exception.InsufficientBalanceException.class,
                () -> walletService.withdraw(
                        1L,
                        new BigDecimal("150.00")
                )
        );

        assertEquals(
                new BigDecimal("100.00"),
                wallet.getBalance()
        );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void withdraw_shouldDecreaseBalanceAndCreateTransaction() {

        Wallet wallet = new Wallet();
        wallet.setUserId(1L);
        wallet.setBalance(new BigDecimal("500.00"));

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(wallet));

        walletService.withdraw(
                1L,
                new BigDecimal("150.00")
        );

        assertEquals(
                new BigDecimal("350.00"),
                wallet.getBalance()
        );

        verify(walletRepository)
                .findByIdForUpdate(1L);

        verify(transactionRepository)
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void transfer_shouldMoveMoneyAndCreateTransaction() {

        Wallet sender = new Wallet();
        sender.setUserId(1L);
        sender.setBalance(new BigDecimal("500.00"));

        Wallet receiver = new Wallet();
        receiver.setUserId(2L);
        receiver.setBalance(new BigDecimal("100.00"));

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(sender));

        when(walletRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(receiver));

        when(idempotencyRecordRepository.tryCreate(
                1L,
                "transfer-123"
        )).thenReturn(1);

        walletService.transfer(
                1L,
                2L,
                new BigDecimal("150.00"),
                "transfer-123"
        );

        assertEquals(
                new BigDecimal("350.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("250.00"),
                receiver.getBalance()
        );

        verify(idempotencyRecordRepository)
                .tryCreate(1L, "transfer-123");

        verify(walletRepository)
                .findByIdForUpdate(1L);

        verify(walletRepository)
                .findByIdForUpdate(2L);

        verify(transactionRepository)
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void transfer_shouldIgnoreDuplicateIdempotencyKey() {

        Wallet sender = new Wallet();
        sender.setUserId(1L);
        sender.setBalance(new BigDecimal("500.00"));

        Wallet receiver = new Wallet();
        receiver.setUserId(2L);
        receiver.setBalance(new BigDecimal("100.00"));

        when(idempotencyRecordRepository.tryCreate(
                1L,
                "transfer-123"
        )).thenReturn(0);

        walletService.transfer(
                1L,
                2L,
                new BigDecimal("150.00"),
                "transfer-123"
        );

        assertEquals(
                new BigDecimal("500.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("100.00"),
                receiver.getBalance()
        );

        verify(idempotencyRecordRepository)
                .tryCreate(1L, "transfer-123");

        verify(walletRepository,
                org.mockito.Mockito.never())
                .findByIdForUpdate(org.mockito.ArgumentMatchers.anyLong());

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void transfer_shouldRejectWhenBalanceIsInsufficient() {

        Wallet sender = new Wallet();
        sender.setUserId(1L);
        sender.setBalance(new BigDecimal("100.00"));

        Wallet receiver = new Wallet();
        receiver.setUserId(2L);
        receiver.setBalance(new BigDecimal("50.00"));

        when(idempotencyRecordRepository.tryCreate(
                1L,
                "transfer-456"
        )).thenReturn(1);

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(sender));

        when(walletRepository.findByIdForUpdate(2L))
                .thenReturn(Optional.of(receiver));

        org.junit.jupiter.api.Assertions.assertThrows(
                com.wallet.exception.InsufficientBalanceException.class,
                () -> walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("150.00"),
                        "transfer-456"
                )
        );

        assertEquals(
                new BigDecimal("100.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("50.00"),
                receiver.getBalance()
        );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }
    @Test
    void transfer_shouldRejectWhenSenderAndReceiverAreSame() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer(
                        1L,
                        1L,
                        new BigDecimal("100.00"),
                        "transfer-789"
                )
        );

        verify(idempotencyRecordRepository,
                org.mockito.Mockito.never())
                .tryCreate(
                        org.mockito.ArgumentMatchers.anyLong(),
                        org.mockito.ArgumentMatchers.anyString()
                );

        verify(walletRepository,
                org.mockito.Mockito.never())
                .findByIdForUpdate(
                        org.mockito.ArgumentMatchers.anyLong()
                );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void transfer_shouldRejectBlankIdempotencyKey() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("100.00"),
                        "   "
                )
        );

        verify(idempotencyRecordRepository,
                org.mockito.Mockito.never())
                .tryCreate(
                        org.mockito.ArgumentMatchers.anyLong(),
                        org.mockito.ArgumentMatchers.anyString()
                );

        verify(walletRepository,
                org.mockito.Mockito.never())
                .findByIdForUpdate(
                        org.mockito.ArgumentMatchers.anyLong()
                );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deposit_shouldRejectZeroAmount() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> walletService.deposit(
                        1L,
                        BigDecimal.ZERO
                )
        );

        verify(walletRepository,
                org.mockito.Mockito.never())
                .findByIdForUpdate(
                        org.mockito.ArgumentMatchers.anyLong()
                );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }
    @Test
    void deposit_shouldRejectAmountWithMoreThanTwoDecimalPlaces() {

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> walletService.deposit(
                        1L,
                        new BigDecimal("100.001")
                )
        );

        verify(walletRepository,
                org.mockito.Mockito.never())
                .findByIdForUpdate(
                        org.mockito.ArgumentMatchers.anyLong()
                );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }
    @Test
    void deposit_shouldThrowWhenWalletDoesNotExist() {

        when(walletRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                com.wallet.exception.WalletNotFoundException.class,
                () -> walletService.deposit(
                        1L,
                        new BigDecimal("100.00")
                )
        );

        verify(transactionRepository,
                org.mockito.Mockito.never())
                .save(org.mockito.ArgumentMatchers.any());
    }
}