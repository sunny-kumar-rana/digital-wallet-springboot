package com.wallet.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.math.BigDecimal;

import com.wallet.service.WalletServiceImplmnt;

@WebServlet("/transfer")
public class TransferController extends HttpServlet {
    private WalletServiceImplmnt walletServiceImplmnt = new WalletServiceImplmnt();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try{
            long senderId = Long.parseLong(request.getParameter("senderId"));
            long receiverId = Long.parseLong(request.getParameter("receiverId"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            walletServiceImplmnt.transfer(senderId, receiverId, amount);

            response.getWriter().println("Transfer Successful!");
        } catch (Exception e) {
            response.setContentType("text/plain");

            e.printStackTrace(response.getWriter());
        }
    }
}
