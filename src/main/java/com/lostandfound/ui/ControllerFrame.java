package com.lostandfound.ui;


import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;
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

    public boolean requestClaim(Post post){
        List<User> claimants = new ArrayList<>();

        // escape if there is no user...:(
        // means something went real wrong
        if(this.user == null){
            JOptionPane.showMessageDialog(this, "you MUST be logged in! >:(");
            return false;
        }

        // make sure ownerID and claimantID are not the same
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

        post.setClaimerID(user.getUserID());
        post.setStatus("pending");
        // update the db
        postRepo.updateClaim(post);

        JOptionPane.showMessageDialog(this, "Request sent! :)");
        return true;

    }

}