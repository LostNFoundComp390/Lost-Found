package com.lostandfound.services;

import com.lostandfound.model.Claim;
import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;

import java.util.ArrayList;
import java.util.List;

public class ClaimService {
    private final DbPostsRepository myRepo;

    public ClaimService(DbPostsRepository myRepo) {
        this.myRepo = myRepo;
    }

    // helper method to move as much claim logic out of controller frame as I find possible
    public String submitClaim(User user, Post post){
        // check log
        if(user == null){
            return "you must be logged in!";
        }

        // check admin status
        if (!user.getAdmin()) {
            // check if the post has been claimed

            if (post.getStatus().equalsIgnoreCase("claimed")) {
                return "This post has already been claimed!";
            }

            // check if the user has already submitted a claim
            if (!myRepo.createClaim(post.getProductID(), user.getUserID())) {
                return "You have already submitted a claim!";
            }

            // everything went well!
            return "claim submitted!";
        }

        // yeah idk we shouldnt have gotten here but I had to return something
        return "Could not save your request :(";
    }

    // get the list of claims
    public List<Claim> getClaims(Post post){
        return myRepo.findClaims(post.getProductID());
    }

    // tell the database
    public void approveClaim(Post post, Claim approvedClaim){
        post.setStatus("Claimed");
        post.setClaimerID(approvedClaim.getUserID());
        myRepo.updateClaim(post);
    }

}
