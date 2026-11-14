package com.lostandfound.ui;

import com.lostandfound.model.Claim;
import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.repo.DbPostsRepository;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.services.ClaimService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewClaimants extends JDialog {
    private final Post post;
    private final List<Claim> claimsList;
    private final DbPostsRepository postRepo;
    private final ClaimService claimServ;
    private Claim claim;
    private JPanel claimsPanel;


    public ViewClaimants(Post post, DbPostsRepository postRepo, ClaimService claimServ) {
        this.post = post;
        this.postRepo = postRepo;
        this.claimServ = claimServ;

        this.claimsList = this.postRepo.findClaims(post.getProductID());
        this.claimsPanel = new JPanel(new GridBagLayout());
        this.claim = null;
    }

    public String showAdmin(){
        // Init
        ButtonGroup group = new ButtonGroup();
        int i = 0;
        String[] options = { "approve", "reject", "cancel"};

        for(Claim claim: this.claimsList){
            // JRadioButton radio = new JRadioButton(claim.getUsername());
            JRadioButton radio = new JRadioButton(claim.toString());

            // tell the radio button which claim is here
            radio.putClientProperty("Claim", claim);
            // group creates a link between all the radio, only one can be selected
            group.add(radio);

            // all radios still must be added to the panel
            this.claimsPanel.add(radio, constraints(
                0, i, 1,0,0, GridBagConstraints.NONE, GridBagConstraints.CENTER));
            // move to the next row
            i++;
        }

        int choice = JOptionPane.showOptionDialog(
                this, // parent comp
                 this.claimsPanel, // list of claims
                "Select a claimant", // title
                JOptionPane.YES_NO_CANCEL_OPTION, // # of buttons
                JOptionPane.PLAIN_MESSAGE, // icon type
                null, // custom icon
                options, // my buttons
                options[2] // default button = cancel
                );

        if (choice == 0){
            approved();
        }else if (choice == 1){
            rejected();
        }else if (choice == 2){
            cancelled();
        }

        if(claim != null){
            return this.claim.getUsername();
        }else{
            return null;
        }

    }

    private void approved() {
        // init
        this.claim = selected();

        // get mad at the user if they are tryna break the program
        if (this.claim == null) {
            JOptionPane.showMessageDialog(this, "You must select a user or cancel");
            return;
        }

        // approve the claim
        this.claimServ.approveClaim(this.post, this.claim);

        // delete the rest
        for (Component comp : this.claimsPanel.getComponents()) {
            if (comp instanceof JRadioButton radio && !radio.isSelected()) {
                Claim rejectedClaim = (Claim) radio.getClientProperty("Claim");
                this.claimServ.deleteClaim(rejectedClaim.getClaimID());
            }
        }

    }

    private void rejected(){
//        for(Component comp: this.claimsPanel.getComponents()){
//            if(comp instanceof JRadioButton radio && radio.isSelected()){
//                this.claim = (Claim) radio.getClientProperty("Claim");
//                this.claimServ.rejectClaim(this.claim.getClaimID());
//            }
//        }
//
        this.claim = selected();
        if (this.claim == null){
            JOptionPane.showMessageDialog(this, "you must select a user or cancel");
        }else{
            this.claimServ.rejectClaim(this.claim.getClaimID());
        }
    }

    private Object cancelled(){
        return null;
    }

    private Claim selected(){
        for(Component comp: this.claimsPanel.getComponents()){
            if(comp instanceof JRadioButton radio && radio.isSelected()){
                // return the selected one
                return (Claim) radio.getClientProperty("Claim");
            }
        }
        // or return nothing
        return null;
    }

    // helper method stolen from another class
    private GridBagConstraints constraints(
            int column,
            int row,
            int width,
            float weightx,
            float weighty,
            int fill,
            int anchor
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        // Add 5 empty pixels as padding around every component
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = width;
        gbc.weightx = weightx; // determines how components share extra space if multiple components can expand into the same space
        gbc.weighty = weighty; // determines how components share extra vertical space
        gbc.fill = fill; // determines if and where a component can expand into extra space
        gbc.anchor = anchor; // determines component alignment within its own cell

        return gbc;
    }
}
