package com.lostandfound.repo;

import com.lostandfound.db.Database;
import com.lostandfound.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbPostsRepository implements PostRepository {
    private final Database db;

    public DbPostsRepository(Database db) {
        this.db = db;
    }

    public long save(Post post) {
        String sql = """ 
               INSERT OR IGNORE INTO Listings (
               status,
               date_listed,
               description,
               category,
               product_name,
               expiration_date,
               user_id,
               photo,
               value_status,
               location)
               VALUES
               (?,?,?,?,?,?,?,?,?,?);
                """;
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, post.getStatus());
            ps.setString(2, post.getPostedString());
            ps.setString(3, post.getDesc());
            ps.setString(4, post.getCategory());
            ps.setString(5, post.getName());
            ps.setString(6, post.getStringExpiration());
            ps.setInt(7, post.getOwnerID());
            ps.setString(8, post.getPhoto());
            ps.setString(9, post.getValueStatus());
            ps.setString(10, post.getLocation());

            int isNewRow = ps.executeUpdate();
            if (isNewRow > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long ID = rs.getLong(1);
                        post.setProductID(ID);
                        return ID;
                    }
                }
            } else {
                sql = "SELECT listing_id FROM Listings WHERE product_name = ? AND user_id = ? AND date_listed = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, post.getName());
                ps.setInt(2, post.getOwnerID());
                ps.setString(3, post.getPostedString());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long ID = rs.getLong(1);
                        post.setProductID(ID);
                        return ID;
                    }
                }
            }
            throw new SQLException("SEEK HELP");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Post findListing(long ID) {
        String sql = "SELECT * FROM Listings WHERE listing_id = ?";
        Post post = new Post();

        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    post = rowToPost(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM Listings";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(rowToPost(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    public List<Post> searchCategory(String category) {
        String sql = "SELECT * FROM Listings WHERE category = ?";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(rowToPost(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    private Post rowToPost(ResultSet rs) throws SQLException {
        Post post = new Post();

        post.setStatus(rs.getString("status"));
        post.setDateOfListing(rs.getString("date_listed"));
        post.setDesc(rs.getString("description"));
        post.setCategory(rs.getString("category"));
        post.setName(rs.getString("product_name"));
        post.setExpirationDate(rs.getString("expiration_date"));
        post.setownerID(rs.getInt("user_id"));
        post.setClaimerID(rs.getInt("claimer_id"));
        post.setProductID(rs.getInt("listing_id"));
        post.setPhoto(rs.getString("photo"));
        post.setValueStatus(rs.getString("value_status"));
        post.setLocation(rs.getString("location"));

        return post;
    }

    public void updateClaim(Post post) {
        String sql = "UPDATE Listings SET status = ?, claimer_id = ? WHERE listing_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, post.getStatus());

            if (post.getClaimerID() > 0) {
                ps.setInt(2, post.getClaimerID());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setLong(3, post.getProductID());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePost(Post post) {
        String sql = """
                UPDATE Listings
                SET description = ?,
                    category = ?,
                    product_name = ?,
                    expiration_date = ?,
                    photo = ?,
                    value_status = ?,
                    location = ?
                WHERE listing_id = ?;
                """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, post.getDesc());
            ps.setString(2, post.getCategory());
            ps.setString(3, post.getName());
            ps.setString(4, post.getStringExpiration());
            ps.setString(5, post.getPhoto());
            ps.setString(6, post.getValueStatus());
            ps.setString(7, post.getLocation());
            ps.setLong(8, post.getProductID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void deletePost(long listingId) {
        String sql = "DELETE FROM Listings WHERE listing_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}