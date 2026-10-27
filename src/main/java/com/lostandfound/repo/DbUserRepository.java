package com.lostandfound.repo;

import com.lostandfound.db.Database;
import com.lostandfound.model.User;

import java.sql.*;
import java.util.List;

/**
 * This class exists to allow CRUD operations on User
 */
public class DbUserRepository implements UserRepository {

    private final Database db;
    private Connection conn;

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
        String sql = """
            """;
        // TODO: return user record
        return null;
    }

    @Override
    public void save(User user) {
        // TODO
    }

    @Override
    public List<User> findAll() {
        // TODO
        return List.of();
    }
}
