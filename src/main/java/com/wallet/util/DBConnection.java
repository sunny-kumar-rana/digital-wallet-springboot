package com.wallet.util.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "wallet";
    private static final String PASSWORD = "Admin";

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.OracleDriver");
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
