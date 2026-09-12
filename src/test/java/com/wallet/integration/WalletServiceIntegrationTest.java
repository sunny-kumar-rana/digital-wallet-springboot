package com.wallet.integration;

import com.wallet.exception.InsufficientBalanceException;
import com.wallet.model.Transaction;
import com.wallet.model.TransactionStatus;
import com.wallet.model.TransactionType;
import com.wallet.model.Wallet;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.service.WalletService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class WalletServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("digital_wallet_test")
                    .withUsername("test_user")
                    .withPassword("test_password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add(
                "spring.jpa.hibernate.ddl-auto",
                () -> "create-drop"
        );
    }

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();

        walletRepository.save(
                new Wallet(1L, new BigDecimal("1000.00"))
        );
    }

    @AfterAll
    static void shutdownExecutor() {
        if (postgres != null) {
            postgres.stop();
        }
    }

    @Test
    void deposit_shouldPersistUpdatedBalanceAndTransaction() {

        walletService.deposit(
                1L,
                new BigDecimal("250.00")
        );

        Wallet wallet = walletRepository
                .findById(1L)
                .orElseThrow();

        assertEquals(
                new BigDecimal("1250.00"),
                wallet.getBalance()
        );

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(1L, 1L);

        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);

        assertEquals(1L, transaction.getSenderId());
        assertEquals(1L, transaction.getReceiverId());
        assertEquals(
                new BigDecimal("250.00"),
                transaction.getAmount()
        );
        assertEquals(
                TransactionType.DEPOSIT,
                transaction.getTransactionType()
        );
        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );
        assertNotNull(transaction.getCreatedAt());
    }

    @Test
    void transfer_shouldPersistBothWalletBalancesAndTransaction() {

        walletRepository.save(
                new Wallet(1L, new BigDecimal("1000.00"))
        );

        walletRepository.save(
                new Wallet(2L, new BigDecimal("500.00"))
        );

        walletService.transfer(
                1L,
                2L,
                new BigDecimal("250.00"),
                "integration-transfer-001"
        );

        Wallet sender = walletRepository
                .findById(1L)
                .orElseThrow();

        Wallet receiver = walletRepository
                .findById(2L)
                .orElseThrow();

        assertEquals(
                new BigDecimal("750.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("750.00"),
                receiver.getBalance()
        );

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(1L, 1L);

        assertEquals(1, transactions.size());

        Transaction transaction = transactions.get(0);

        assertEquals(1L, transaction.getSenderId());
        assertEquals(2L, transaction.getReceiverId());
        assertEquals(
                new BigDecimal("250.00"),
                transaction.getAmount()
        );
        assertEquals(
                TransactionType.TRANSFER,
                transaction.getTransactionType()
        );
        assertEquals(
                TransactionStatus.SUCCESS,
                transaction.getStatus()
        );
        assertNotNull(transaction.getCreatedAt());
    }

    @Test
    void transfer_shouldIgnoreDuplicateIdempotencyKey() {

        walletRepository.save(
                new Wallet(2L, new BigDecimal("500.00"))
        );

        String idempotencyKey = "integration-idempotency-001";

        walletService.transfer(
                1L,
                2L,
                new BigDecimal("250.00"),
                idempotencyKey
        );

        walletService.transfer(
                1L,
                2L,
                new BigDecimal("250.00"),
                idempotencyKey
        );

        Wallet sender = walletRepository
                .findById(1L)
                .orElseThrow();

        Wallet receiver = walletRepository
                .findById(2L)
                .orElseThrow();

        assertEquals(
                new BigDecimal("750.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("750.00"),
                receiver.getBalance()
        );

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(1L, 1L);

        assertEquals(1, transactions.size());
    }

    @Test
    void concurrentTransfers_shouldPreserveBalanceConsistency()
            throws Exception {

        walletRepository.save(
                new Wallet(2L, new BigDecimal("500.00"))
        );

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> transfer1 = executor.submit(() ->
                walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("600.00"),
                        "concurrent-transfer-001"
                )
        );

        Future<?> transfer2 = executor.submit(() ->
                walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("600.00"),
                        "concurrent-transfer-002"
                )
        );

        int successfulTransfers = 0;

        for (Future<?> future : new Future[]{transfer1, transfer2}) {
            try {
                future.get();
                successfulTransfers++;
            } catch (Exception ignored) {
                // One transfer must fail because the sender only has 1000.00.
            }
        }

        executor.shutdown();

        Wallet sender = walletRepository
                .findById(1L)
                .orElseThrow();

        Wallet receiver = walletRepository
                .findById(2L)
                .orElseThrow();

        assertEquals(1, successfulTransfers);
        assertEquals(
                new BigDecimal("400.00"),
                sender.getBalance()
        );
        assertEquals(
                new BigDecimal("1100.00"),
                receiver.getBalance()
        );

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(1L, 1L);

        assertEquals(1, transactions.size());
    }

    @Test
    void transfer_shouldRejectWhenBalanceIsInsufficient() {

        walletRepository.save(
                new Wallet(2L, new BigDecimal("500.00"))
        );

        assertThrows(
                InsufficientBalanceException.class,
                () -> walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("1500.00"),
                        "integration-insufficient-001"
                )
        );

        Wallet sender = walletRepository
                .findById(1L)
                .orElseThrow();

        Wallet receiver = walletRepository
                .findById(2L)
                .orElseThrow();

        assertEquals(
                new BigDecimal("1000.00"),
                sender.getBalance()
        );

        assertEquals(
                new BigDecimal("500.00"),
                receiver.getBalance()
        );

        assertEquals(0, transactionRepository.count());
    }

    @Test
    void transfer_shouldRejectSelfTransfer() {

        assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer(
                        1L,
                        1L,
                        new BigDecimal("100.00"),
                        "integration-self-transfer-001"
                )
        );

        Wallet wallet = walletRepository
                .findById(1L)
                .orElseThrow();

        assertEquals(
                new BigDecimal("1000.00"),
                wallet.getBalance()
        );

        assertEquals(0, transactionRepository.count());
    }

    @Test
    void transfer_shouldRejectBlankIdempotencyKey() {

        walletRepository.save(
                new Wallet(2L, new BigDecimal("500.00"))
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> walletService.transfer(
                        1L,
                        2L,
                        new BigDecimal("100.00"),
                        "   "
                )
        );

        assertEquals(0, transactionRepository.count());
    }
}