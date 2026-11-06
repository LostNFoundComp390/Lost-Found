import com.lostandfound.db.Database;
import com.lostandfound.db.DatabaseInitializer;
import com.lostandfound.model.Post;
import com.lostandfound.repo.DbPostsRepository;


import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PostTest {
    // Initialization
    private DbPostsRepository myRepo;
    private Post testPost;

    @BeforeEach
    public void createDb() throws SQLException {
        // Initialize the test data and the db
        Database db = Database.production();
        DatabaseInitializer.initialize(db);
        myRepo = new DbPostsRepository(db);
        testPost = new Post(
                "Unclaimed",
                "2026-09-28",
                "Black Hydroflask" ,
                "Accessories",
                "Water Bottle",
                "2026-10-28",
                3,
                "hydroflask.jpg",
                "Standard",
                "DMF");
    }

    @Test
    public void testSaveQueryDb(){
        // get the productID while saving the post into the database
        long ID = myRepo.save(testPost);
        Post dbPost = myRepo.findListing(ID);

        // Do the actual retrieval checks
        assertEquals("Unclaimed", dbPost.getStatus());
        assertEquals("2026-09-28", dbPost.getDateOfListing());
        assertEquals("Black Hydroflask", dbPost.getDesc());
        assertEquals("Accessories", dbPost.getCategory());
        assertEquals("Water Bottle", dbPost.getName());
        assertEquals("2026-10-28",dbPost.getExpirationDate());
        assertEquals(3, dbPost.getUserID());
        assertEquals("hydroflask.jpg", dbPost.getPhoto());
        assertEquals("Standard", dbPost.getValueStatus());
        assertEquals("DMF", dbPost.getLocation());

        // Check the original post type too, code should update the productID in the original
        assertEquals(ID, testPost.getProductID());

    }
}
