package com.wallet.controller;


import com.wallet.service.WalletServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/balance")
public class WalletController extends HttpServlet {
    private WalletServiceImpl walletServiceImpl = new WalletServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try{
            HttpSession session = request.getSession(false);
            if(session == null){
                throw new RuntimeException("Not Logged In");
            }
            long userId = (Long) session.getAttribute("userId");
            BigDecimal balance = walletServiceImpl.getBalance(userId);

            PrintWriter pw = response.getWriter();
            pw.println("Balance = " + balance);

        } catch (Exception e) {
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
        }

    }
}
