package com.wallet.controller;

import com.wallet.service.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.wallet.dto.RegisterRequestDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationController extends HttpServlet {
    private UserServiceImpl userServiceImpl = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        try{
            RegisterRequestDto dto = new RegisterRequestDto();

            dto.setName(request.getParameter("name"));
            dto.setEmail(request.getParameter("email"));
            dto.setPassword(request.getParameter("password"));

            userServiceImpl.register(dto);

            PrintWriter pw = response.getWriter();
            pw.println("User Registered Successfully");

        } catch (SQLException e) {

            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());

        } catch (ClassNotFoundException e) {
            System.out.println(e);;
        }
    }
}
