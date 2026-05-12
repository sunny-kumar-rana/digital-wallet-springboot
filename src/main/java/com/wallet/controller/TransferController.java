package com.wallet.controller;

import com.wallet.dto.TransferRequestDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.math.BigDecimal;

import com.wallet.service.WalletServiceImpl;

@WebServlet("/transfer")
public class TransferController extends HttpServlet {
    private WalletServiceImpl walletServiceImpl = new WalletServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/plain");

        try{
            TransferRequestDto dto = new TransferRequestDto();

            dto.setSenderId(Long.parseLong(request.getParameter("senderId")));
            dto.setReceiverId(Long.parseLong(request.getParameter("receiverId")));
            dto.setAmount(new BigDecimal(request.getParameter("amount")));

            if(dto.getSenderId() == dto.getReceiverId()){
                response.getWriter().println("Sender and receiver cannot be same");
                return;
            }

            walletServiceImpl.transfer(
                    dto.getSenderId(),
                    dto.getReceiverId(),
                    dto.getAmount()
            );

            response.getWriter().println("Transfer Successful!");
        } catch (Exception e) {

            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());

        }
    }
}
