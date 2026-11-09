package com.lostandfound.ui;


import com.lostandfound.model.Post;
import com.lostandfound.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ControllerFrame extends JFrame{
    public final String login = "LOGIN";
    public final String browsing = "BROWSING";

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BrowseListings browsingPanel;
    private LoginUI loginPanel;

    public ControllerFrame(UserService userService, List<Post> posts) throws IOException{
        // Initialization
        cardLayout = new CardLayout();
        mainPanel = new JPanel((cardLayout));

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
        cardLayout.show(mainPanel, login);
    }

    public void showBrowsing(boolean isAdmin){
        // Browsing panel must know if user is an admin
        browsingPanel.setAdmin(isAdmin);

        // Show the screen
        cardLayout.show(mainPanel, browsing);
    }

}