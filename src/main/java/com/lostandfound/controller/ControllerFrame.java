package com.lostandfound.controller;


import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.model.UserSession;
import com.lostandfound.repo.DbPostsRepository;
import com.lostandfound.services.PostService;
import com.lostandfound.services.UserService;
import com.lostandfound.ui.BrowseListings;
import com.lostandfound.ui.LoginUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ControllerFrame extends JFrame{
    public final String login = "LOGIN";
    public final String browsing = "BROWSING";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BrowseListings browsingPanel;
    private LoginUI loginPanel;

    private DbPostsRepository postRepo;

    public ControllerFrame(UserService userService, PostService postService) throws IOException{
        // Initialization
        cardLayout = new CardLayout();
        mainPanel = new JPanel((cardLayout));

        //postRepo = activeRepo;

        setTitle("Lost-Found");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500,500));


        // Passing this instance of the controller frame to the login class so they may talk to each other
        // This initializes an instance of the login screen

        loginPanel = new LoginUI(this, userService);
        browsingPanel = new BrowseListings(this, userService, postService);

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
        UserSession.getInstance().logout();
        // log out
        cardLayout.show(mainPanel, login);
    }

    public void showBrowsing(){
        browsingPanel.updateAdminVisibility();
        browsingPanel.loadPosts();
        // Show the screen
        cardLayout.show(mainPanel, browsing);
    }


    public boolean requestClaim(Post post){

        User user = UserSession.getInstance().getCurrentUser();

        // escape if there is no user...:(
        // means something went real wrong
        if(user == null){
            JOptionPane.showMessageDialog(this, "you MUST be logged in! >:(");
            return false;
        }

        // make sure ownerID and claimantID are not the same
        if(user.getUserID() == post.getOwnerID()){
            JOptionPane.showMessageDialog(this, "You may not claim your own collection :(");
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