package com.lostandfound.repo;
import com.lostandfound.db.Database;
import com.lostandfound.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class DbPostsRepository implements PostRepository{
    private final Database db;
    public DbPostsRepository(Database db) {
        this.db = db;
    }

    public long save(Post post){
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
        try (Connection conn = db.getConnection()){


            // Compile
            PreparedStatement ps = conn.prepareStatement(sql);
            // Assign vars
            ps.setString(1,post.getStatus());
            ps.setString(2, post.getPostedString());
            ps.setString(3, post.getDesc());
            ps.setString(4,post.getCategory());
            ps.setString(5,post.getName());
            ps.setString(6, post.getStringExpiration());
            ps.setInt(7, post.getOwnerID());
            ps.setString(8,post.getPhoto());
            ps.setString(9,post.getValueStatus());
            ps.setString(10,post.getLocation());

            // Check to see if a new row was actually created, prevents dupe data
            int isNewRow = ps.executeUpdate();
            if (isNewRow>0){
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        long ID = rs.getLong(1);
                        post.setProductID(ID);
                        return ID;
                    }
                }
            // This would have been duped data, so send back the row that alr exists
            }else{
                sql = "SELECT listing_id FROM listings WHERE product_name = ? AND user user_id = ? AND date_listed = ?";
                // Compile
                ps = conn.prepareStatement(sql);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        long ID = rs.getLong(1);
                        post.setProductID(ID);
                        return ID;
                    }
                }
            }
            // If we have made it this far w/o seeing the productID then something has gone terribly wrong
            throw new SQLException("SEEK HELP");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // Search the DB for a single post based on the ID
    // Needed this for debug
    public Post findById(long ID){
        // Initialization
        String sql = "SELECT * FROM Listings WHERE listing_id = ?";
        Post post = new Post();

        try(Connection conn = db.getConnection()){
            // Compile
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, ID);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    post = rowToPost(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }


    // Searches for all posts
    public List<Post> findAll(){
        String sql = """
            SELECT * FROM Listings
            """;
        List<Post> posts = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                posts.add(rowToPost(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving posts", e);
        }

        return posts;
    }

    public List<Post> findAllNonExpired(){
        // Todo: add Where is_expired = false
        String sql = """
            SELECT * FROM Listings
            """;
        List<Post> posts = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                posts.add(rowToPost(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving non-expired posts", e);
        }

        return posts;
    }

    public List<Post> searchCategory(String category) {
        // Initialization
        String sql = "SELECT * FROM Listings WHERE category = ?";
        List<Post> posts = new ArrayList<>();

        // Try to connect to the db
        try (Connection conn = db.getConnection();
            // Compile sql query
            PreparedStatement ps = conn.prepareStatement(sql)) {
            // Insert the category the user selected to sort by
            ps.setString(1, category);
            // Try to actually query
            try(ResultSet rs = ps.executeQuery()) {
                // While there are still rows to search
                while (rs.next()) {
                    // Create a new post object with the helper method
                    Post post = rowToPost(rs);

                    // Add posts to the list
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    @Override
    public List<Post> searchByKeyword(String keyword) {
        String pattern = "%" + keyword + "%";
        String sql = """
            SELECT *
            FROM Listings
            WHERE (product_name like ? OR description like ?)
            """;


        List<Post> posts = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post post = rowToPost(rs);

                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching posts by keyword", e);
        }
        return posts;

    }

    @Override
    public List<Post> searchByKeywordNonExpired(String keyword) {
        // todo: sql will include a where is_expired = false
        String pattern = "%" + keyword + "%";
        String sql = """
            SELECT *
            FROM Listings
            WHERE (product_name like ? OR description like ?)
            """;


        List<Post> posts = new ArrayList<>();

        // Try to connect to the db
        try (Connection conn = db.getConnection();
             // Compile sql query
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Insert the category the user selected to sort by
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            // Try to actually query
            try(ResultSet rs = ps.executeQuery()) {
                // While there are still rows to search
                while (rs.next()) {
                    // Create a new post object with the helper method
                    Post post = rowToPost(rs);

                    // Add posts to the list
                    posts.add(post);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching non-expired posts by keyword", e);
        }
        return posts;

    }

    // Helper method for turning rows into a Post
    private Post rowToPost(ResultSet rs) throws SQLException{
        // Initialization
        Post post = new Post();

        // Assign attributes
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

        // return final object
        return post;
    }

    public void updateClaim(Post post) {
        String sql = "UPDATE Listings SET status = ?, claimer_id = ? WHERE listing_id = ?";

        // Try to connect to the db
        try (Connection conn = db.getConnection();
             // compile SQL
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // assign values
            ps.setString(1, post.getStatus());

            if (post.getClaimerID() > 0) {
                // assign the user
                ps.setInt(2, post.getClaimerID());
            } else {
                // or set null if there is no claimant somehow
                ps.setNull(2, Types.INTEGER);
            }

            ps.setLong(3, post.getProductID());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    }
