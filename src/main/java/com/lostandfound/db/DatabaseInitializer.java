package com.lostandfound.db;

import com.password4j.Password;

import java.sql.*;

public class DatabaseInitializer {
    public static void initialize(Database database) throws SQLException {
        try (Connection conn = database.getConnection()) {

            createUserTable(conn);
            createListingsTable(conn);
            addMockUsers(conn);
            addMockPosts(conn);
        }

    }

    private static void createUserTable(Connection conn) {
        String sql = """
            CREATE TABLE IF NOT EXISTS Users (
            user_id INTEGER PRIMARY KEY AUTOINCREMENT,
            role TEXT NOT NULL,
            username TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            email TEXT NOT NULL
            )
            """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void createListingsTable(Connection conn) {
        String sql = """
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
            """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addMockUsers(Connection conn) {

        String sql = "INSERT OR IGNORE INTO Users (role, username, password, email) VALUES(?, ?, ?, ?)";

        String[][] userRecords = {
            {"Admin", "admin", "password1234", "admin@campus.edu"},
            {"User", "michael", "dontleavemehere", "michael@campus.edu"},
            {"Admin", "DMFAdmin", "DanaMohlerFaria1234", "dmf_admin@campus.edu"},
            {"Admin", "Alex", "BlueSky2026", "alex_admin@campus.edu"},
            {"User", "Jordan", "coffeeandcode", "jordan@campus.edu"},
            {"User", "Taylor", "SunnyDay123", "taylor@campus.edu"},
            {"Admin", "Sam", "CampusAdmin1", "sam_admin@campus.edu"},
            {"User", "Casey", "letmeinplease", "casey@campus.edu"}
        };


        try {
            PreparedStatement pstm = conn.prepareStatement(sql);

            for (String[] record : userRecords) {
                String role = record[0];
                String username = record[1];
                String plainTextPassword = record[2];
                String email = record[3];

                String hashedPassword = Password.hash(plainTextPassword).withBcrypt().getResult();

                pstm.setString(1, role);
                pstm.setString(2, username);
                pstm.setString(3, hashedPassword);
                pstm.setString(4, email);

                pstm.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static void addMockPosts(Connection conn){

        String sql = """
            INSERT OR IGNORE INTO Listings (listing_id, product_name, photo, description, location, category, status, date_listed, expiration_date, value_status, user_id) VALUES
            (101, 'Water Bottle', 'hydroflask.jpg', 'Black Hydroflask', 'DMF', 'Accessories', 'Unclaimed', '2026-09-28', '2026-10-28', 'Standard', 3),
            (102, 'Airpods', 'airpods.jpg', 'White AirPods Pro 3rd Generation', 'Burnell Hall', 'Electronics', 'Unclaimed', '2026-10-01', '2026-11-01', 'High', 1),
            (103, 'Laptop', 'macbook.jpg', 'Blue Apple MacBook Air', 'Maxwell Library', 'Electronics', 'Claimed', '2026-10-03', '2026-11-03', 'High', 7),
            (104, 'Wireless headphones', 'headphones.jpg', 'Pink noise canceling overear headphones', 'RSU', 'Electronics', 'Unclaimed', '2026-10-09', '2027-01-01', 'High', 1),
            (105, 'Vintage leather jacket', 'jacket.jpg', 'Genuine brown leather and men''s medium', 'Bear''s Den RSU', 'Clothing', 'Unclaimed', '2026-06-13', '2026-10-15', 'Standard', 3),
            (106, 'Canvas tote bag', 'canvas_tote_bag.jpg', 'Large cream-colored canvas tote bag with navy-blue shoulder straps and a small embroidered university logo near the bottom corner. The bag contains a spiral notebook with handwritten class notes, a reusable stainless-steel coffee cup, several pens, and a black zippered pouch. It was found resting underneath a table near a study area.', 'Maxwell Library', 'Accessories', 'Unclaimed', '2026-10-09', '2026-11-09', 'Standard', 4),
            (108, 'Winter coat', 'winter_coat.jpg', 'Dark green insulated winter coat with a hood trimmed in faux fur, two large front pockets with snap closures, and a silver zipper that runs down the center. A pair of knit gloves was found inside the left pocket. The coat appears lightly worn and has no visible personal identification on the outside.', 'Hunt Hall', 'Clothing', 'Unclaimed', '2026-10-07', '2027-01-07', 'Standard', 3),
            (109, 'Black backpack', 'black_backpack.jpg', 'Medium-sized black backpack with padded shoulder straps, a front organizer pocket, two mesh side pockets, and a reflective strip across the lower front panel. Inside are several empty folders, a charging cable, a calculator, and a notebook with no clearly visible owner name. The bag was left near a seating area.', 'Crimson Hall', 'Bags', 'Unclaimed', '2026-10-01', '2026-11-01', 'Low', 7),
            (110, 'Black sunglasses', 'blacksunglasses.jpg', 'Standard cheap pair of sunglasses.','RSU','Accessories','Unclaimed','2026-12-06','2027-01-06','Low',3);
           """;


        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
