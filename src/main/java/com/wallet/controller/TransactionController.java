package com.wallet.controller;

import com.wallet.model.Transaction;
import com.wallet.service.WalletServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet("/transactions")
public class TransactionController extends HttpServlet {

    WalletServiceImpl walletServiceImpl = new WalletServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try{
            long userId = Long.parseLong(request.getParameter("userId"));

            List<Transaction> txs = walletServiceImpl.getTransactionHistory(userId);

            PrintWriter pw = response.getWriter();
            for(Transaction t : txs){

                pw.println("----------------------------");
                pw.println("Transaction ID : " + t.getId());
                pw.println("Sender ID      : " + t.getSenderId());
                pw.println("Receiver ID    : " + t.getReceiverId());
                pw.println("Amount         : " + t.getAmount());
                pw.println("Status         : " + t.getStatus());
                pw.println("Created At     : " + t.getCreatedAt());

            }
        } catch (Exception e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
        }
    }
}
