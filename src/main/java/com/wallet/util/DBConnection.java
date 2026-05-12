package com.wallet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {

        try{
            Properties properties = new Properties();

            InputStream input =
                    DBConnection.class.getClassLoader()
                            .getResourceAsStream("db.properties");

            if(input == null){
                throw new RuntimeException("db.properties file not found");
            }

            properties.load(input);

            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            Class.forName(driver);

            return DriverManager.getConnection(url, username, password);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
