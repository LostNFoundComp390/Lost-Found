    package com.lostandfound.controller;


    import com.lostandfound.model.Claim;
    import com.lostandfound.model.Post;
    import com.lostandfound.model.User;
    import com.lostandfound.model.UserSession;
    import com.lostandfound.repo.DbPostsRepository;
    import com.lostandfound.repo.PostRepository;
    import com.lostandfound.services.PostService;
    import com.lostandfound.services.ClaimService;
    import com.lostandfound.services.UserService;
    import com.lostandfound.ui.BrowseListings;
    import com.lostandfound.ui.LoginUI;
    import com.lostandfound.ui.ViewClaimants;

    import javax.swing.*;
    import java.awt.*;
    import java.io.IOException;
    import java.util.List;

    import javax.swing.filechooser.FileNameExtensionFilter;
    import java.io.File;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardCopyOption;


    public class ControllerFrame extends JFrame{
        public final String login = "LOGIN";
        public final String browsing = "BROWSING";

        private CardLayout cardLayout;
        private JPanel mainPanel;
        private BrowseListings browsingPanel;
        private LoginUI loginPanel;

        private final ClaimService claimServ;
        private PostRepository postRepo;
        public ControllerFrame(UserService userService, PostService postService, ClaimService claimServ) throws IOException{
            // Initialization
            cardLayout = new CardLayout();
            mainPanel = new JPanel((cardLayout));
            this.claimServ = claimServ;


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
            setLocationRelativeTo(null);
            setVisible(true);
        }

        public void showLogin(){
            // clear the user
            UserSession.getInstance().logout();
            // log out
            cardLayout.show(mainPanel, login);
            pack();
        }

        public void showBrowsing(){
            browsingPanel.updateAdminVisibility();
            browsingPanel.loadPosts();

            // Show the screen
            cardLayout.show(mainPanel, browsing);

            // fills the screen
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        /*public boolean requestClaim(Post post){
            // escape if there is no user...:(
            // means something went real wrong
            if(user == null){
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

        public boolean claimButton(Post post){
            User currentUser = UserSession.getInstance().getCurrentUser();

            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "You MUST be logged in to perform this action!");
                return false;
            }

            if(currentUser.isAdmin()){
                // init the claims
                List<Claim> claims = claimServ.getClaims(post);

                // check if there are claims
                if (claims.isEmpty()){
                    JOptionPane.showMessageDialog(this,"There are no claims to view!");
                    return false;
                }

                // there must be claims, therefore deploy the radio button dialogue
                ViewClaimants viewer = new ViewClaimants(post, claimServ);
                viewer.showAdmin();
                return false;
            }else{
                String feedback = claimServ.submitClaim(currentUser, post);
                JOptionPane.showMessageDialog(this, feedback);

                // postComp needs a boolean to disable the button after a claim was submitted
                if(feedback.equalsIgnoreCase("claim submitted!") ||
                    feedback.equalsIgnoreCase("You have already submitted a claim!")){
                    return true;
                }
            }

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

            // this allows users to click submit claim again if their claim failed and allows admins to always do view claimants
            return false;
        }

        public boolean canModifyPost(Post post) {
            User user = UserSession.getInstance().getCurrentUser();
            if (user == null || post == null) {
                return false;
            }
            return user.getUserID() == post.getOwnerID() || user.isAdmin();
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
            //JTextField photoField = new JTextField(post.getPhoto() != null ? post.getPhoto() : "", 20);
            JTextField valueStatusField = new JTextField(post.getValueStatus() != null ? post.getValueStatus() : "Standard", 20);

            JTextArea descArea = new JTextArea(post.getDesc(), 2, 20);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            JScrollPane descScroll = new JScrollPane(descArea);

            // Image picker & preview setup
            JTextField photoField = new JTextField(post.getPhoto() != null ? post.getPhoto() : "",14);
            photoField.setEditable(false);
            JButton browseImageBtn = new JButton("Change Image...");
            JLabel imagePreview = new JLabel();
            imagePreview.setPreferredSize(new Dimension(40,40));
            imagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            imagePreview.setHorizontalAlignment(SwingConstants.CENTER);

            // Load current image if one exists
            if (post.getPhoto() != null && !post.getPhoto().trim().isEmpty()) {
                String firstPhoto = post.getPhoto().split(",")[0].trim();
                File existingFile = new File("images/" + firstPhoto);
                if (existingFile.exists()) {
                    Image scaled = new ImageIcon(existingFile.getAbsolutePath()).getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(scaled));
                }
            }

            // Track newly selected file (null if manager keeps existing photo)
            final File[] selectedImageFile = {null};

            browseImageBtn.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Select Item Image");
                chooser.setFileFilter(new FileNameExtensionFilter("Image Files )JPG, PNG, GIF)", "jpg", "jpeg", "png","gif"));

                if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    selectedImageFile[0] = chooser.getSelectedFile();
                    photoField.setText(selectedImageFile[0].getName());

                    // Update Preview Thumbnail
                    Image scaled = new ImageIcon(selectedImageFile[0].getAbsolutePath()).getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH);
                    imagePreview.setIcon(new ImageIcon(scaled));

                }

            });

            JPanel photoControlPanel = new JPanel(new BorderLayout(5,5));
            photoControlPanel.add(photoField, BorderLayout.CENTER);
            photoControlPanel.add(browseImageBtn, BorderLayout.EAST);

            JPanel photoWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT,5,0));
            photoWrapper.add(imagePreview);
            photoWrapper.add(photoControlPanel);


            // Layout the dialog form
            JPanel formPanel = new JPanel(new GridLayout(0,2,8,8));
            formPanel.add(new JLabel("Item Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Category:"));
            formPanel.add(categoryBox);
            formPanel.add(new JLabel("Location:"));
            formPanel.add(locationField);
            formPanel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
            formPanel.add(expirationField);
            formPanel.add(new JLabel("Item Image:"));
            formPanel.add(photoWrapper);
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

                // If a new image was picked, copy it into the "images/" folder
                String finalPhotoName = post.getPhoto();
                if (selectedImageFile[0] != null) {
                    try {
                        File imagesDir = new File("images");
                        if (!imagesDir.exists()) {
                            imagesDir.mkdirs();
                        }
                        // Prefix with timestamp so identically named files don't overwrite eachother
                        String uniqueFileName = System.currentTimeMillis() + "_" + selectedImageFile[0].getName();
                        Path destination = Paths.get(imagesDir.getAbsolutePath(), uniqueFileName);
                        Files.copy(selectedImageFile[0].toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                        finalPhotoName = uniqueFileName;
                    }   catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Failed to save new image: " + ex.getMessage(), "Image Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // Update the Post object in memory
                post.setName(newName);
                post.setCategory((String) categoryBox.getSelectedItem());
                post.setLocation(newLocation);
                post.setExpirationDate(newExp);
                post.setPhoto(finalPhotoName);
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
