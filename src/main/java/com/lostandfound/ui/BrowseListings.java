package com.lostandfound.ui;
// Comp 390
// Browse listings page where users can browse their lost and found items

import com.lostandfound.model.Post;
import com.lostandfound.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BrowseListings extends JPanel {
    private final ControllerFrame controls;
    private final List<Post> allPosts;
    private final JPanel searchPanel;

    private JPanel listModel;
    private JPanel listingsPanel;
    private JPanel headerPanel;
    private JPanel footerPanel;
    private JPanel addPostPanel;
    private JPanel centerTitle;

    private Font myFont = new Font("Tahoma", Font.PLAIN, 20);
    private JScrollPane scrollPane;

    private JButton logout;
    private JButton electronicsButton;
    private JButton accessoriesButton;
    private JButton clothingsButton;
    private JButton bagsButton;
    private JButton allButton;
    private JButton createPost;

    private User user;
    private List<PostComponent> postComps;


    // Turning BrowseListings into a panel to work with controller Frame
    public BrowseListings(ControllerFrame controller, List<Post> posts) {
        // Set up of main panel
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialization
        JLabel title = new JLabel("Campus Lost & Found");

        this.controls = controller;
        this.allPosts = posts;
        this.postComps = new ArrayList<>();

        headerPanel = new JPanel();
        searchPanel = new JPanel(new GridBagLayout());
        listingsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // allows dynamic sizing with auto sorting, 3 across and infinite down
        listModel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(listingsPanel);
        footerPanel = new JPanel(new GridBagLayout());

        // Overlay layout lets title be centered while the button is all the way to the right
        headerPanel.setLayout(new OverlayLayout(headerPanel));

        centerTitle = new JPanel(new GridBagLayout());
        addPostPanel = new JPanel(new GridBagLayout());


        logout = new JButton("Log Out");
        accessoriesButton = new JButton("Accessories");
        electronicsButton = new JButton("Electronics");
        clothingsButton = new JButton("Clothing");
        bagsButton = new JButton("Bags");
        allButton = new JButton("All");
        createPost = new JButton("+");

        listModel.add(accessoriesButton, constraints(0, 0, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(electronicsButton, constraints(0, 1, 1, 0,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(clothingsButton, constraints(0, 2, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(bagsButton, constraints(0, 3, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(allButton, constraints(0, 4, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));

        // Action Listener to switch panels
        logout.addActionListener(event -> {
            controls.showLogin();
        });

        // action Listener for listing categories
        accessoriesButton.addActionListener(event -> {
            filterByCategory("Accessories");
        });

        bagsButton.addActionListener(event -> {
            filterByCategory("Bags");
        });

        clothingsButton.addActionListener(event -> {
            filterByCategory("Clothing");
        });

        electronicsButton.addActionListener(event -> {
            filterByCategory("Electronics");
        });

        allButton.addActionListener(event -> {
            filterByCategory("All");
        });

        // Set up of centered title
        centerTitle.setOpaque(false);
        centerTitle.add(title, constraints(0,0,1,1,0,GridBagConstraints.NONE, GridBagConstraints.CENTER));

        // Set up of right most button
        addPostPanel.setOpaque(false);
        addPostPanel.add(createPost, constraints(0,0,1,1,0, GridBagConstraints.NONE, GridBagConstraints.EAST));

        headerPanel.add(centerTitle);
        headerPanel.add(addPostPanel);

        // Fix the cooked scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(25);



        // add logout to the footerPanel and give it a weight y
        // footerPanel forces logout to be at the far right even if the cell its in is weirdly sized
        footerPanel.add(logout, constraints(0,0,1,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_END));
        searchPanel.add(listModel);

        // Add everything to the main panel
        // add(headerPanel, BorderLayout.NORTH);
        // add(scrollPane, BorderLayout.CENTER);
        // add(searchPanel, BorderLayout.WEST);
        // add(footerPanel, BorderLayout.SOUTH);

        // Add the header to the top row
        // Header panel should be wide and be at the top of the page
        add(headerPanel, constraints(0,0,3,1,0,GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTH));

        // Add the search panel to the left area
        // Search panel should be long and be at the left of the page
        add(searchPanel, constraints(0,1,1,0,0,GridBagConstraints.NONE, GridBagConstraints.NORTHWEST));

        // Add the scroll pane below the header (includes the posts)
        // Should be long and wide
        // Should be in the center of the page
        // Must leave room for the footer panel
        // BOTH means it can expand in both directions
        add(scrollPane, constraints(1,1,2,1,1,GridBagConstraints.BOTH, GridBagConstraints.CENTER));

        // Add the footer panel to the bottom
        // Should be wide
        // Should be at the bottom of the page
        add(footerPanel, constraints(0,2,3,1,0,GridBagConstraints.BOTH,GridBagConstraints.SOUTH));

        // Fonts
        title.setFont(myFont.deriveFont(Font.BOLD, 24));
        logout.setFont(myFont);

        // Adding posts
        setPosts(posts);
    }

    private void filterByCategory(String category) {
        if (category.equalsIgnoreCase("All")) {
            setPosts(allPosts);
            return;
        }

        List<Post> filteredPosts = new ArrayList<>();

        for (Post post : allPosts) {
            if (post.getCategory() != null
                    && post.getCategory().equalsIgnoreCase(category)) {
                filteredPosts.add(post);
            }
        }
        setPosts(filteredPosts);
    }

    public void setPosts(List<Post> posts) {
        // Initialization
        JLabel emptyLabel = new JLabel("No listings are available");
        emptyLabel.setFont(myFont);

        // Delete everything if there is anything, will be useful later with add/deleting
        listingsPanel.removeAll();

        // If there are no posts, provide feedback. Will also be useful later
        if (posts.isEmpty()) {
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            listingsPanel.add(emptyLabel, constraints(0, 0, 1, 1,0, GridBagConstraints.HORIZONTAL, GridBagConstraints.PAGE_START));
        } else {
            for (Post post : posts) {
                // add all the new components. Constraints set on listing earlier forces organization
                // listingsPanel.add(new PostComponent(post, controls));
                PostComponent pc = new PostComponent(post,controls);
                postComps.add(pc);
                listingsPanel.add(pc);
            }
        }

        // update page
        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    // Helper method stolen from postComponents
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

    // Helper method to add the + button if the user is an admin
 /*   public void setAdmin(boolean isAdmin){
        // avoid adding it twice if somehow its still there
        addPostPanel.setVisible(isAdmin);

        // Rebuild
        headerPanel.revalidate();
        headerPanel.repaint();

        this.revalidate();
        this.repaint();
    }*/

    public void setUser(User user){
        // assign the current user
        this.user = user;

        // update the little components
        for(PostComponent pc: postComps){
            pc.setUser(this.user);
            pc.setClaimButton();
        }

        // build or rebuild depending on the user role
        addPostPanel.setVisible(user.getAdmin());

        headerPanel.revalidate();
        headerPanel.repaint();

        this.revalidate();
        this.repaint();
    }

    public User getUser(){
        return this.user;
    }
}

