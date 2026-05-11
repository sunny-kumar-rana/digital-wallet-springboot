package com.wallet.service;

import com.wallet.exception.InsufficientBalanceException;
import com.wallet.exception.WalletNotFoundException;
import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WalletServiceImpl implements WalletService{
    private WalletRepository walletRepository = new WalletRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    public void transfer(long senderId, long receiverId, BigDecimal amount) throws SQLException, ClassNotFoundException {

        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if(senderId == receiverId){
            throw new IllegalArgumentException("Sender and receiver cannot be same");
        }

        Connection conn = DBConnection.getConnection();

        try{
            conn.setAutoCommit(false);

            Wallet sender = walletRepository.findByUserIdForUpdate(conn,senderId);
            Wallet receiver = walletRepository.findByUserIdForUpdate(conn,receiverId);

            if(sender == null){
                throw new WalletNotFoundException("Sender wallet not found");
            }

            if(receiver == null){
                throw new WalletNotFoundException("Receiver wallet not found");
            }

            if(sender.getBalance().compareTo(amount) < 0){
                throw new InsufficientBalanceException("Insufficient balance");
            }
            walletRepository.updateBalance(conn, senderId, sender.getBalance().subtract(amount));
            walletRepository.updateBalance(conn, receiverId, receiver.getBalance().add(amount));

            transactionRepository.createTransaction(conn, senderId, receiverId, amount, "SUCCESS");
            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        finally {
            conn.close();
        }
    }

    public BigDecimal getBalance(long userId) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getConnection();

        try{
            Wallet wallet = walletRepository.findByUserId(conn, userId);

            if (wallet == null){
                throw new WalletNotFoundException("Wallet not found");
            }

            return wallet.getBalance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
    }

    public List<Transaction> getTransactionHistory(long userId) throws ClassNotFoundException, SQLException {
        Connection conn = DBConnection.getConnection();

        try{
            return transactionRepository.findByUser(conn, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }

    }
}
