import com.lostandfound.db.Database;
import com.lostandfound.db.DatabaseInitializer;
import com.lostandfound.model.Claim;
import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;


import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.repo.UserRepository;
import com.lostandfound.services.ClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;


public class PostTest {
    // Initialization
    // Delete this db right after we are done using it
    @TempDir
    Path tempDir;
    private Database db;
    private DbPostsRepository myRepo;
    private DbUserRepository userRepo;
    private ClaimService claimServ;
    private Post testPost;

    @BeforeEach
    public void createDb() throws SQLException {
        // Initialize the test data and the db
        Path testDbFile = tempDir.resolve("test.db");
        db = Database.test(testDbFile.toString());
        DatabaseInitializer.initialize(db);

        myRepo = new DbPostsRepository(db);
        userRepo = new DbUserRepository(db);
        claimServ = new ClaimService(myRepo);

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
        assertEquals("2026-09-28", dbPost.getPostedString());
        assertEquals("Black Hydroflask", dbPost.getDesc());
        assertEquals("Accessories", dbPost.getCategory());
        assertEquals("Water Bottle", dbPost.getName());
        assertEquals("2026-10-28",dbPost.getStringExpiration());
        assertEquals(3, dbPost.getOwnerID());
        assertEquals("hydroflask.jpg", dbPost.getPhoto());
        assertEquals("Standard", dbPost.getValueStatus());
        assertEquals("DMF", dbPost.getLocation());

        // Check the original post type too, code should update the productID in the original
        assertEquals(ID, testPost.getProductID());
    }

    @Test
    public void testRequestClaimUpdatesStatusAndClaimer() {
        long id = myRepo.save(testPost);

        // make user 5 send a request
        testPost.setClaimerID(5);
        testPost.setStatus("Pending");
        myRepo.updateListingStatus(testPost);

        // make sure the stuff was changed
        Post updated = myRepo.findListing(id);
        assertEquals("Pending", updated.getStatus());
        assertEquals(5, updated.getClaimerID());
    }

    @Test
    public void testRejectClaimResetsPost() {
        long id = myRepo.save(testPost);

        // pull the test data
        testPost.setClaimerID(5);
        testPost.setStatus("Pending");
        myRepo.updateListingStatus(testPost);

        // pretend to reject the post
        testPost.setClaimerID(0);
        testPost.setStatus("Unclaimed");
        myRepo.updateListingStatus(testPost);

        // expect rejection values
        Post rejected = myRepo.findListing(id);
        assertEquals("Unclaimed", rejected.getStatus());
        assertEquals(0, rejected.getClaimerID());
    }

    @Test
    public void testDeleteClaim(){
        // Init
        long postID = myRepo.save(testPost);

        myRepo.createClaim(postID, 2);

        List<Claim> claimsList = myRepo.findClaims(postID);
        long claimID = claimsList.get(0).getClaimID();

        // delete the claim
        myRepo.deleteClaim(claimID);
        List<Claim> deletedClaimsList = myRepo.findClaims(postID);

        // check the lists
        assertEquals(1,claimsList.size());
        assertTrue(deletedClaimsList.isEmpty());
    }

    @Test
    public void testApproveClaims(){
        // Init
        long postID = myRepo.save(testPost);

        myRepo.createClaim(postID, 2);
        myRepo.createClaim(postID,5);
        myRepo.createClaim(postID,6);

        List<Claim> claimsList = myRepo.findClaims(postID);
        Claim approved = claimsList.get(0);
        Claim rejected = claimsList.get(1);
        Claim deleted = claimsList.get(2);

        // update the db
        claimServ.approveClaim(testPost, approved);
        claimServ.rejectClaim(rejected.getClaimID());
        claimServ.deleteClaim(deleted.getClaimID());

        // check the db records
        Post dbPost = myRepo.findListing(postID);

        // Check for expected results
        assertEquals("claimed",dbPost.getStatus());
        assertEquals(1, myRepo.findClaims(postID).size()); // should be the 1 rejected claim
        assertFalse(myRepo.createClaim(postID,6));
    }

    @Test
    public void claimValidation(){
        // init
        long postID = myRepo.save(testPost);
        User regularUser = userRepo.findByUsername("michael");
        User regularUser1 = userRepo.findByUsername("Jordan");
        User admin = userRepo.findByUsername("admin");

        // deny admin claims
        assertEquals("Could not save your request :(", claimServ.submitClaim(admin, testPost));

        // deny second attempts
        assertEquals("claim submitted!", claimServ.submitClaim(regularUser, testPost));
        assertEquals("You have already submitted a claim!", claimServ.submitClaim(regularUser, testPost));

        // mark post as claimed
        testPost.setStatus("claimed");
        myRepo.updateListingStatus(testPost);
        assertEquals("This post has already been claimed!", claimServ.submitClaim(regularUser1, testPost));
    }
}
