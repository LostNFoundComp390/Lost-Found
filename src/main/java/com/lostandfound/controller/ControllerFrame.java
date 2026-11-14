    package com.lostandfound.controller;


    import com.lostandfound.model.Claim;
    import com.lostandfound.model.Post;
    import com.lostandfound.model.User;
    import com.lostandfound.model.UserSession;
    import com.lostandfound.repo.DbPostsRepository;
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

    public class ControllerFrame extends JFrame{
        public final String login = "LOGIN";
        public final String browsing = "BROWSING";

        private CardLayout cardLayout;
        private JPanel mainPanel;
        private BrowseListings browsingPanel;
        private LoginUI loginPanel;

        private final ClaimService claimServ;


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
                List<Claim> claims = this.claimServ.getClaims(post);

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

    }
