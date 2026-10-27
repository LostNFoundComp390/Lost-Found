package com.lostandfound.db;

import java.sql.*;

public class DatabaseInitializer {
    public static void initialize(Database database) throws SQLException {
        try (Connection conn = database.getConnection(); Statement st = conn.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS Users (
                        user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        role TEXT NOT NULL,
                        username TEXT NOT NULL UNIQUE,
                        password TEXT NOT NULL,
                        email TEXT NOT NULL
                    )
                """);
            st.execute("""
                    CREATE TABLE IF NOT EXISTS Listings (
                        listing_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        product_name TEXT NOT NULL,
                        photo TEXT,
                        description TEXT,
                        location TEXT,
                        category TEXT,
                        status TEXT,
                        date_listed TEXT,
                        expiration_date TEXT,
                        value_status TEXT,
                        user_id INTEGER NOT NULL,
                        FOREIGN KEY(user_id) REFERENCES Users(user_id)
                    )
                """);

            // Populate with default values
            st.execute("""
                    INSERT OR IGNORE INTO Users (role, username, password, email) VALUES
                    ('Admin', 'admin', 'password1234', 'admin@campus.edu'),
                    ('User', 'michael', 'dontleavemehere', 'michael@campus.edu'),
                    ('Admin', 'DMFAdmin', 'DanaMohlerFaria1234', 'dmf_admin@campus.edu'),
                    ('Admin', 'Alex', 'BlueSky2026', 'alex_admin@campus.edu'),
                    ('User', 'Jordan', 'coffeeandcode', 'jordan@campus.edu'),
                    ('User', 'Taylor', 'SunnyDay123', 'taylor@campus.edu'),
                    ('Admin', 'Sam', 'CampusAdmin1', 'sam_admin@campus.edu'),
                    ('User', 'Casey', 'letmeinplease', 'casey@campus.edu')
                """);
        }
    }
}
