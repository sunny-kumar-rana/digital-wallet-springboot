package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/login")
public class AuthController extends HttpServlet {

    UserServiceImpl userServiceImpl = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userServiceImpl.login(email, password);

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            response.getWriter().println("Login Successful");

        } catch (Exception e) {
            response.getWriter().println(e);
        }

    }
}
