package com.lostandfound.ui;
// Comp 390
// Browse listings page where users can browse their lost and found items

import com.lostandfound.controller.ControllerFrame;
import com.lostandfound.model.Post;
import com.lostandfound.model.User;
import com.lostandfound.model.UserSession;
import com.lostandfound.services.PostService;
import com.lostandfound.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BrowseListings extends JPanel {
    private final ControllerFrame controls;
    private final List<Post> allPosts;

    private final JPanel categoryPanel;
    private final JPanel searchPanel;
    private final JPanel listModel;
    private final JPanel listingsPanel;
    private final JPanel headerPanel;
    private final JPanel footerPanel;
    private final JPanel addPostPanel;

    private final Font myFont = new Font("Tahoma", Font.PLAIN, 20);
    private final JScrollPane scrollPane;

    private final JButton logoutButton;
    private final JButton electronicsButton;
    private final JButton accessoriesButton;
    private final JButton clothingsButton;
    private final JButton bagsButton;
    private final JButton allButton;
    private final JButton createPostButton;
    private final JButton searchButton;

    private final JTextField searchField;
    private final JLabel searchLabel;

    private final PostService postService;
    private final UserService userService;
    private User user;

    private String currentCategory;
    private String currentKeyword;


    // Turning BrowseListings into a panel to work with controller Frame
    public BrowseListings(ControllerFrame controller, UserService userService, PostService postService) {
        // Set up of main panel
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.currentCategory = "All";
        this.currentKeyword = null;

        this.controls = controller;
        this.postService = postService;
        this.userService = userService;
        this.allPosts = postService.findAll();

        headerPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel = new JPanel(new BorderLayout(5, 0));
        categoryPanel = new JPanel(new GridBagLayout());
        listingsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // allows dynamic sizing with auto sorting, 3 across and infinite down
        listModel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(listingsPanel);
        footerPanel = new JPanel(new GridBagLayout());

        // Search bar
        searchField = new JTextField();
        searchLabel = new JLabel("Search: ");

        addPostPanel = new JPanel(new GridBagLayout());

        searchButton = new JButton("Search");
        createPostButton = new JButton("+");
        accessoriesButton = new JButton("Accessories");
        electronicsButton = new JButton("Electronics");
        clothingsButton = new JButton("Clothing");
        bagsButton = new JButton("Bags");
        allButton = new JButton("All");
        logoutButton = new JButton("Log Out");

        listModel.add(accessoriesButton, constraints(0, 0, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(electronicsButton, constraints(0, 1, 1, 0,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(clothingsButton, constraints(0, 2, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(bagsButton, constraints(0, 3, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(allButton, constraints(0, 4, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));

        // Action Listener to switch panels
        logoutButton.addActionListener(event -> {
            UserSession.getInstance().logout();
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

        searchField.addActionListener(event -> {
            makeSearch();
        });
        searchButton.addActionListener(event -> {
            makeSearch();
        });


        // Set up of right most button
        addPostPanel.setOpaque(false);
        addPostPanel.add(createPostButton, constraints(0,0,1,1,0, GridBagConstraints.NONE, GridBagConstraints.EAST));

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        headerPanel.add(addPostPanel, BorderLayout.EAST);

        // Fix the cooked scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(25);



        // add logout to the footerPanel and give it a weight y
        // footerPanel forces logout to be at the far right even if the cell its in is weirdly sized
        footerPanel.add(logoutButton, constraints(0,0,1,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_END));
        categoryPanel.add(listModel);

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
        add(categoryPanel, constraints(0,1,1,0,0,GridBagConstraints.NONE, GridBagConstraints.NORTHWEST));

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

        updateAdminVisibility();

        // Fonts
        logoutButton.setFont(myFont);

        // Adding posts
        setPosts(allPosts);
    }

    private void makeSearch() {
        String keyword = searchField.getText();
        currentKeyword = keyword;
        if(keyword.isBlank()) return;
        setPosts(postService.search(keyword));
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

    private void applyFilter(){
        //postService.filterPost(currentKeyword, currentCategory);
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
                listingsPanel.add(new PostComponent(post, controls));
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

    /**
     * This will handle what component can be seen by an admin
     */
    public void updateAdminVisibility() {
        User user = UserSession.getInstance().getCurrentUser();

        boolean isAdmin = (user != null && user.isAdmin());
        addPostPanel.setVisible(isAdmin);

        headerPanel.revalidate();
        headerPanel.repaint();
        this.revalidate();
        this.repaint();
    }

}

