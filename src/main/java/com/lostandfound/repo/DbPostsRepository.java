package com.lostandfound.repo;
import com.lostandfound.db.Database;
import com.lostandfound.model.Claim;
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
                sql = "SELECT listing_id FROM listings WHERE product_name = ? AND user_id = ? AND date_listed = ?";
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
        // Todo: handle not found post
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

    public List<Claim> findClaims(long listingID){
        // init return var
        List<Claim> claims = new ArrayList<>();

        // prep sql
        String sql = """ 
                SELECT 
                c.claim_id,
                c.user_id,
                u.username,
                c.status
                FROM Claims c JOIN Users u ON u.user_id = c.user_id
                WHERE c.listing_id = ?
                """;
        // connect to db
        try (Connection conn = db.getConnection()){
            // prep sql
            PreparedStatement ps = conn.prepareStatement(sql);

            // insert input var into sql
            ps.setLong(1, listingID);

            // seek results
            try(ResultSet rs = ps.executeQuery()){
                // while there are still rows returned
                while(rs.next()){
                    // create a claim object of the current row
                    Claim claim = new Claim(
                            rs.getLong("claim_id"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("status"));

                    // insert claim into list
                    claims.add(claim);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return claims;
    }

    public boolean createClaim(long listingID, int userID){
        // coalesce will turn a null value into '', even though there should be no nulls
        // <> claimed will reject any claim attempts if the item is already claimed
        String sql = """
                INSERT INTO Claims (listing_id, user_id, status)
                SELECT listing_id, ?, 'pending' FROM Listings WHERE
                listing_id = ? 
                    AND LOWER(COALESCE(status, '')) <> 'claimed'
                ON CONFLICT(listing_id, user_id) DO NOTHING
                """;

        // connect to db
        try (Connection conn = db.getConnection()){
            // prep sql
            PreparedStatement ps = conn.prepareStatement(sql);

            // insert vars
            ps.setInt(1, userID);
            ps.setLong(2, listingID);

            // return feedback based on if ps updates one and only one row
            if(ps.executeUpdate() == 1){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
