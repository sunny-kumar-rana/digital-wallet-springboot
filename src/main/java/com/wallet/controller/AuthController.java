package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserServiceImplmnt;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/login")
public class AuthController extends HttpServlet {

    UserServiceImplmnt userServiceImplmnt = new UserServiceImplmnt();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = userServiceImplmnt.login(email, password);

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            response.getWriter().println("Login Successful");

        } catch (Exception e) {
            response.getWriter().println(e);
        }

    }
}
