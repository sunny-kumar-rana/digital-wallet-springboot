package com.wallet.service;

import com.wallet.repository.TransactionRepository;
import com.wallet.repository.WalletRepository;
import com.wallet.model.Transaction;
import com.wallet.model.Wallet;
import com.wallet.util.dbconnection.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class WalletServiceImplmnt implements WalletService{
    private WalletRepository walletDao = new WalletRepository();
    private TransactionRepository transactionDao = new TransactionRepository();

    public void transfer(long senderId, long receiverId, BigDecimal amount) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.getConnection();

        try{
            conn.setAutoCommit(false);

            Wallet sender = walletDao.findByUserIdForUpdate(conn,senderId);
            Wallet receiver = walletDao.findByUserIdForUpdate(conn,receiverId);

            if(sender.getBalance().compareTo(amount) < 0){
                throw new RuntimeException("Insufficient Balance!");
            }
            walletDao.updateBalance(conn, senderId, sender.getBalance().subtract(amount));
            walletDao.updateBalance(conn, receiverId, receiver.getBalance().add(amount));

            transactionDao.createTransaction(conn, senderId, receiverId, amount, "SUCCESS");
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
            Wallet wallet = walletDao.findByUserId(conn, userId);

            if (wallet == null){
                throw new RuntimeException("Wallet not Found");
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
            return transactionDao.findByUser(conn, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }

    }
}
