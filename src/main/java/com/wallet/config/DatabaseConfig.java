package com.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DatabaseConfig {

    @Bean
    public Connection connection() throws Exception {

        Class.forName("oracle.jdbc.OracleDriver");

        return DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:xe",
                "wallet",
                "Admin"
        );
    }
}