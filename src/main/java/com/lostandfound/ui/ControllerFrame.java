package com.lostandfound.ui;


import com.lostandfound.model.Claim;
import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;
import com.lostandfound.services.ClaimService;
import com.lostandfound.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerFrame extends JFrame{
    public final String login = "LOGIN";
    public final String browsing = "BROWSING";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BrowseListings browsingPanel;
    private LoginUI loginPanel;

    private User user;
    private DbPostsRepository postRepo;


    public ControllerFrame(UserService userService, List<Post> posts, DbPostsRepository activeRepo) throws IOException{
        // Initialization
        cardLayout = new CardLayout();
        mainPanel = new JPanel((cardLayout));
        postRepo = activeRepo;

        setTitle("Lost-Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500,500));


        // Passing this instance of the controller frame to the login class so they may talk to each other
        // This initializes an instance of the login screen

        loginPanel = new LoginUI(this, userService);
        browsingPanel = new BrowseListings(this, posts);

        // Add enter key support
        getRootPane().setDefaultButton(loginPanel.getLoginButton());

        // Add our cards to the controller
        mainPanel.add(loginPanel, login);
        mainPanel.add(browsingPanel, browsing);

        // put the container into the frame
        setContentPane(mainPanel);

        // start on the login screen
        showLogin();

        // show everything :)!!!!
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showLogin(){
        // clear the user
        this.user = null;
        // log out
        cardLayout.show(mainPanel, login);
    }

    public void showBrowsing(User user){
        // Browsing panel must know the user
        browsingPanel.setUser(user);
        // saved the logged in user
        this.user=user;

        // Show the screen
        cardLayout.show(mainPanel, browsing);
    }

    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){return this.user;}

    /*public boolean requestClaim(Post post){
        // escape if there is no user...:(
        // means something went real wrong
        if(this.user == null){
            JOptionPane.showMessageDialog(this, "you MUST be logged in! >:(");
            return false;
        }

        // Allow view claimant stuff
        if(user.getAdmin()){
            // JOptionPane.showMessageDialog(this, "You cannot make claims!");

            return false;
        }

        // make sure the post isn't already claimed
        if(post.getStatus().equalsIgnoreCase("claimed")){
            JOptionPane.showMessageDialog(this, "post has been claimed!");
            return false;
        }

        // If we've made it here then everything is okay and normal
        post.setStatus("pending");

        // update the db
        // postRepo.updateClaim(post);
        // post.setClaimerID(user.getUserID());


        JOptionPane.showMessageDialog(this, "Request sent! :)");
        return true;

    }*/

    public boolean requestClaim(Post post){
        // init
        ClaimService claimServ = new ClaimService(postRepo);
        JOptionPane.showMessageDialog(this, claimServ.submitClaim(this.user, post));

        // check the user role

/*        if(this.user == null){
            JOptionPane.showMessageDialog(this, "you MUST be logged in! >:(");
            return false;
        }

        // check the claim status
        if(post.getStatus().equalsIgnoreCase("claimed")){
            JOptionPane.showMessageDialog(this, "This item has recently been claimed!");
            return false;
        }

        // check the admin status
        if(user.getAdmin()){
            // will have to do the view claimant stuff
            List<Claim> claims = postRepo.findClaims(post.getProductID());

            return false;
        }

        // If we've made it here then the user is allowed to submit a claim
        // Try to create the claim
        try{
            boolean created = postRepo.createClaim(post.getProductID(), user.getUserID());

            // The claim failed bcuz the user already requested it
            if (!created){
                JOptionPane.showMessageDialog(this, "You have already requested!");
                return false;
            }

            // The claim succeeded otherwise
            JOptionPane.showMessageDialog(this, "Request sent!");
            return true;
        } catch (RuntimeException e) {
            // proper error handling to satisfy sprint 4 req
            e.printStackTrace();

            JOptionPane.showMessageDialog(this,"Could not save your request.","Database Unreachable", JOptionPane.ERROR_MESSAGE);
        }*/
        return false;
    }

}