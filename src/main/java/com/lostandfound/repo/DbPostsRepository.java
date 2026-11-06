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

    // Save a new post into the database
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
            ps.setString(2, post.getDateOfListing());
            ps.setString(3, post.getDesc());
            ps.setString(4,post.getCategory());
            ps.setString(5,post.getName());
            ps.setString(6, post.getExpirationDate());
            ps.setInt(7, post.getUserID());
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
    public Post findListing(long ID){
        // Initialization
        String sql = "SELECT * FROM Listings WHERE listing_id = ?";
        Post post = new Post();

        try(Connection conn = db.getConnection()){
            // Compile
            PreparedStatement ps = conn.prepareStatement(sql);
            // Insert the ID
            ps.setLong(1, ID);
            // Try to query
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
        // Initialization
        String sql = "SELECT * FROM Listings";
        List<Post> posts = new ArrayList<>();

        // Connect to the db
        try (Connection conn = db.getConnection()) {
            // Compile sql
            PreparedStatement ps = conn.prepareStatement(sql);
            // Try to query
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()) {
                    // Create new post
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

    // Searches the database for the requested category then returns a list of all the rows
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
        post.setUserID(rs.getInt("user_id"));
        post.setProductID(rs.getInt("listing_id"));
        post.setPhoto(rs.getString("photo"));
        post.setValueStatus(rs.getString("value_status"));
        post.setLocation(rs.getString("location"));

        // return final object
        return post;
    }

}
