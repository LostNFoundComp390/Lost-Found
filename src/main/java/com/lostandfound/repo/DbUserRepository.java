package com.lostandfound.repo;

import com.lostandfound.db.Database;
import com.lostandfound.model.User;
import com.lostandfound.model.UserPublic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class exists to allow CRUD operations on User
 */
public class DbUserRepository implements UserRepository {

    private final Database db;

    public DbUserRepository(Database db) {
        this.db = db;
    }

    @Override
    public boolean exists(String username) {
        String sql = "SELECT 1 FROM Users WHERE Username = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findByUsername(String username) {
        if (!exists(username)) return null;

        String sql = "SELECT role, username, password, email FROM Users WHERE username = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return User.of(
                    rs.getString("role"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        String sql = """
            INSERT INTO Users (role, username, password, email) VALUES(?, ?, ?, ?)
            ON CONFLICT(username)
            DO UPDATE SET
                role = excluded.role
                password = excluded.password
                email = excluded.email
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getRole());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserPublic> findAll() {
        List<UserPublic> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()) {
                users.add(
                    UserPublic.of(
                        rs.getString("role"),
                        rs.getString("username"),
                        rs.getString("email")
                )
            );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void delete(String username){

        String sql = "DELETE FROM Users WHERE username = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
