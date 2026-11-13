package com.lostandfound.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String jdbcUrl;

    public Database(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public static Database production() {
        return new Database("jdbc:sqlite:lost-found.db");
    }

    public Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection(jdbcUrl);
        c.createStatement().execute("PRAGMA foreign_keys = ON");
        return c;
    }
}
