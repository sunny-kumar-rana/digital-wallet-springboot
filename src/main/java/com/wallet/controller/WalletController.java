package com.wallet.controller;


import com.wallet.service.WalletServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/balance")
public class WalletController extends HttpServlet {
    private WalletServiceImpl walletServiceImpl = new WalletServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response){

        try{
            HttpSession session = request.getSession(false);
            if(session == null){
                throw new RuntimeException("Not Logged In");
            }
            long userId = (Long)request.getSession().getAttribute("userId");
            BigDecimal balance = walletServiceImpl.getBalance(userId);

            PrintWriter pw = response.getWriter();
            pw.println("Balance = " + balance);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
