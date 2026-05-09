package com.wallet.controller;

import com.wallet.model.User;
import com.wallet.service.UserServiceImplmnt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationController extends HttpServlet {
    private UserServiceImplmnt userServiceImplmnt = new UserServiceImplmnt();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            User user = new User();

            user.setName(request.getParameter("name"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));

            userServiceImplmnt.register(user);

            PrintWriter pw = response.getWriter();
            pw.println("User Registered Successfully");

        } catch (SQLException e) {
            e.printStackTrace(response.getWriter());
        } catch (ClassNotFoundException e) {
            System.out.println(e);;
        }
    }
}
