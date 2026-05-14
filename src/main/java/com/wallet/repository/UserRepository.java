package com.wallet.repository;

import com.wallet.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {
    public long createUser(Connection conn, User user) throws SQLException{
        String query = "INSERT INTO users (id, name, email, password, created_at) VALUES (user_seq.NEXTVAL, ?, ?, ?, CURRENT_TIMESTAMP)";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1,user.getName());
        ps.setString(2,user.getEmail());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ResultSet rs = conn.createStatement().executeQuery("SELECT user_seq.CURRVAL FROM dual");
        rs.next();

        return rs.getLong(1);
    }
    public User findUserByEmail(Connection conn, String email) throws SQLException {
        String query = "SELECT id, name, email, password FROM users WHERE email = ?";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1,email);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            User user = new User();

            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));

            return user;
        }

        return null;
    }
}
