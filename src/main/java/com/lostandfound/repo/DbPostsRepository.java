package com.lostandfound.repo;

import com.lostandfound.db.Database;
import com.lostandfound.model.Claim;
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
            // This would have been duped data, so send back the row that alr exists
            }else{
                sql = "SELECT listing_id FROM listings WHERE product_name = ? AND user_id = ? AND date_listed = ?";
                // Compile
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

    // Search the DB for a single post based on the ID
    // Needed this for debug
    public Post findById(long ID){
        // Initialization
        String sql = "SELECT * FROM Listings WHERE listing_id = ?";
        Post post = new Post();

        try (Connection conn = db.getConnection()) {
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

    public List<Post> findAllUser(){
        String sql = """
            SELECT * FROM Listings
            WHERE is_expired = 0 AND status <> 'claimed'
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

    @Override
    public void updateExpiredStatus() {
        String sql = """
            UPDATE Listings SET is_expired = 1 WHERE expiration_date <= date('now') AND is_expired = 0;
            """;

        try(Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update expired post status", e);
        }
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
    public List<Post> searchByKeywordUser(String keyword) {
        String pattern = "%" + keyword + "%";
        String sql = """
            SELECT *
            FROM Listings
            WHERE (is_expired = 0) AND (product_name like ? OR description like ?) AND status <> 'claimed'
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

    public List<Post> searchByLocationUser(String location){
        // Init
        String locationPattern = "%" + location.toUpperCase() + "%";
        String sql = """
                SELECT * 
                FROM Listings 
                WHERE (is_expired = 0) 
                    AND UPPER(location) LIKE ? 
                    AND LOWER(status) <> 'claimed'
                """;
        List<Post> posts = new ArrayList<>();
        try(Connection conn = db.getConnection()){
            // prepare sql
            PreparedStatement ps = conn.prepareStatement(sql);

            // insert var
            ps.setString(1,locationPattern);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    // create new object
                    Post post = rowToPost(rs);

                    // build the array
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error searching non-expired and unclaimed posts by location",e);
        }

        // send back up the array
        return posts;
    }

    // Helper method for turning rows into a Post
    private Post rowToPost(ResultSet rs) throws SQLException{
        // Initialization
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

    public void updateListingStatus(Post post) {
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
    // Todo: we want to mark the post as not active instead of actually deleting. The admin will need to search old posts.
    public void deletePostById(long listingId) {
        String sql = "DELETE FROM Listings WHERE listing_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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


    public List<Claim> findClaims(long listingID){
        // init return var
        List<Claim> claims = new ArrayList<>();

        // prep sql
        String sql = """ 
                SELECT 
                c.claim_id,
                c.listing_id,
                c.user_id,
                u.username,
                c.status
                FROM Claims c JOIN Users u ON u.user_id = c.user_id
                WHERE c.listing_id = ?
                    AND (c.status IS NULL OR LOWER(TRIM(c.status)) <> 'claimed')
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
                    //     public Claim(long claimID, long postID, int userID, String username, String status) {
                    Claim claim = new Claim(
                            rs.getLong("claim_id"),
                            rs.getLong("listing_id"),
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

    public void updateClaim(long claimID, String status){
        // prep sql
        String sql = """
                UPDATE claims 
                SET STATUS = ?
                WHERE claim_id = ?
                """;
        try(Connection conn = db.getConnection()){
            // prep sql
            PreparedStatement ps = conn.prepareStatement(sql);

            // assign vars
            ps.setString(1, status);
            ps.setLong(2, claimID);
            // update the db
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteClaim(long claimID){
        // prep the sql
        String sql = "DELETE FROM Claims WHERE claim_id = ? ";

        try(Connection conn = db.getConnection()){
            // prep sql
            PreparedStatement ps = conn.prepareStatement(sql);

            // insert var
            ps.setLong(1, claimID);

            // delete the row
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
