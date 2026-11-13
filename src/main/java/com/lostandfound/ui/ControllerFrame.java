package com.lostandfound.ui;


import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;
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
        // saved the logged in user
        this.user=user;
        // Browsing panel must know the user
        browsingPanel.setUser(user);


        // Show the screen
        cardLayout.show(mainPanel, browsing);
    }

    public void setUser(User user){
        this.user = user;
    }

    public boolean requestClaim(Post post){
        // escape if there is no user...:(
        // means something went real wrong
        if(this.user == null){
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

    public boolean canModifyPost(Post post) {
        if (this.user == null || post == null) {
            return false;
        }
        return user.getUserID() == post.getOwnerID() || user.getAdmin();
    }

    public void editPost(Post post) {
        if (!canModifyPost(post)) {
            JOptionPane.showMessageDialog(this, "You can only edit your own posts.");
            return;
        }

        // Pre-filled form fields
        JTextField nameField = new JTextField(post.getName(), 20);
        String[] categories = {"Accessories", "Electronics", "Clothing", "Bags"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setSelectedItem(post.getCategory());

        JTextField locationField = new JTextField(post.getLocation(), 20);
        JTextField expirationField = new JTextField(post.getStringExpiration(), 20);
        JTextField photoField = new JTextField(post.getPhoto() != null ? post.getPhoto() : "", 20);
        JTextField valueStatusField = new JTextField(post.getValueStatus() != null ? post.getValueStatus() : "Standard", 20);

        JTextArea descArea = new JTextArea(post.getDesc(), 4, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);

        // Layout the dialog form
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.add(new JLabel("Item Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        formPanel.add(expirationField);
        formPanel.add(new JLabel("Photo Filename:"));
        formPanel.add(photoField);
        formPanel.add(new JLabel("Value Status:"));
        formPanel.add(valueStatusField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descScroll);

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "Edit Post",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newDesc = descArea.getText().trim();
            String newLocation = locationField.getText().trim();
            String newExp = expirationField.getText().trim();

            if (newName.isEmpty() || newDesc.isEmpty() || newLocation.isEmpty() || newExp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, location, expiration date, and description cannot be empty.");
                return;
            }

            // Update the Post object in memory
            post.setName(newName);
            post.setCategory((String) categoryBox.getSelectedItem());
            post.setLocation(newLocation);
            post.setExpirationDate(newExp);
            post.setPhoto(photoField.getText().trim());
            post.setValueStatus(valueStatusField.getText().trim());
            post.setDesc(newDesc);

            // Persist to SQLite and refresh UI
            postRepo.updatePost(post);
            browsingPanel.refreshPosts();
            JOptionPane.showMessageDialog(this, "Post updated!");
        }
    }
    public void deletePost(Post post) {
        if (!canModifyPost(post)) {
            JOptionPane.showMessageDialog(this, "You can only delete your own posts.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete \"" + post.getName() + "\"?",
                "Delete Post",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            postRepo.deletePost(post.getProductID());
            browsingPanel.removePost(post);
            JOptionPane.showMessageDialog(this, "Post deleted!");
        }
    }

}