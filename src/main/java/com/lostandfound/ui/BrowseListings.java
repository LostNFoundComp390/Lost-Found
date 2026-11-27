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
    private List<Post> allPosts;

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
    private final JButton resetButton;
    private final JButton createPostButton;
    private final JButton searchButton;

    private final JButton dmfButton;
    private final JButton rsuButton;
    private final JButton huntButton;
    private final JButton crimButton;
    private final JButton maxwellButton;
    private final JButton burnellButton;

    private final JTextField searchField;
    private final JLabel searchLabel;

    private final PostService postService;
    private final UserService userService;
    private User user;
    private List<PostComponent> postComps;

    private String currentCategory;
    private String currentKeyword;
    private String currentLocation;


    // Turning BrowseListings into a panel to work with controller Frame
    public BrowseListings(ControllerFrame controller, UserService userService, PostService postService) {
        // Set up of main panel
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.currentCategory = null;
        this.currentKeyword = null;
        this.currentLocation = null;

        this.controls = controller;
        this.postService = postService;
        this.userService = userService;
        this.postComps = new ArrayList<>();

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
        JLabel locations = new JLabel("Location Search");
        JLabel categories = new JLabel("Categories Search");

        addPostPanel = new JPanel(new GridBagLayout());

        createPostButton = new JButton("+");

        searchButton = new JButton("Search");
        accessoriesButton = new JButton("Accessories");
        electronicsButton = new JButton("Electronics");
        clothingsButton = new JButton("Clothing");
        bagsButton = new JButton("Bags");
        resetButton = new JButton("Reset");

        dmfButton = new JButton("DMF");
        rsuButton = new JButton("RSU");
        huntButton = new JButton("Hunt Hall");
        crimButton = new JButton("Crimson Hall");
        maxwellButton = new JButton("Library");
        burnellButton = new JButton("Burnell Hall");

        logoutButton = new JButton("Log Out");

        // Add the category buttons
        listModel.add(categories, constraints(0,0,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.CENTER));
        listModel.add(accessoriesButton, constraints(0, 1, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(electronicsButton, constraints(0, 2, 1, 0,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(clothingsButton, constraints(0, 3, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(bagsButton, constraints(0, 4, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));

        // Add the location buttons
        listModel.add(locations, constraints(0,5,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.CENTER));
        listModel.add(dmfButton, constraints(0,6,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(rsuButton, constraints(0,7,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(huntButton, constraints(0,8,1,1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(crimButton, constraints(0,9,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(maxwellButton, constraints(0,10,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(burnellButton, constraints(0,11,1,1,1,GridBagConstraints.BOTH, GridBagConstraints.LINE_START));
        listModel.add(resetButton, constraints(0, 12, 1, 1,1, GridBagConstraints.BOTH, GridBagConstraints.LINE_START));


        // Action Listener to switch panels
        logoutButton.addActionListener(event -> {
            UserSession.getInstance().logout();
            controls.showLogin();
        });

        // action Listeners for listing categories
        accessoriesButton.addActionListener(event -> {
            selectCategory("Accessories");
        });

        bagsButton.addActionListener(event -> {
            selectCategory("Bags");
        });

        clothingsButton.addActionListener(event -> {
            selectCategory("Clothing");
        });

        electronicsButton.addActionListener(event -> {
            selectCategory("Electronics");
        });

        resetButton.addActionListener(event -> {
            resetFilters();
        });

        searchField.addActionListener(event -> {
            makeSearch();
        });
        searchButton.addActionListener(event -> {
            makeSearch();
        });


        // action listeners for locations
        dmfButton.addActionListener(event -> {
            selectLocation("DMF");
        });
        rsuButton.addActionListener(event ->{
            selectLocation("RSU");

        });
        huntButton.addActionListener(event ->{
            selectLocation("Hunt");
        });
        crimButton.addActionListener(event ->{
            selectLocation("Crimson");
        });
        maxwellButton.addActionListener(event ->{
            selectLocation("Maxwell");
        });
        burnellButton.addActionListener(event ->{
            selectLocation("Burnell");
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

        // Fonts
        logoutButton.setFont(myFont);

    }

    private void selectCategory(String category) {
        if (category.equalsIgnoreCase(currentCategory)) {
            currentCategory = null;
        } else {
            currentCategory = category;
        }
        updateFilterButtonStyles();
        makeSearch();
    }

    private void selectLocation(String location) {
        if (location.equalsIgnoreCase(currentLocation)) {
            currentLocation = null;
        } else {
            currentLocation = location;
        }
        updateFilterButtonStyles();
        makeSearch();
    }

    private void resetFilters() {
        currentCategory = null;
        currentLocation = null;
        currentKeyword = null;
        searchField.setText("");
        updateFilterButtonStyles();
        makeSearch();
    }

    private void makeSearch() {
        currentKeyword = searchField.getText();
        setPosts(postService.search(currentKeyword, currentCategory, currentLocation));
    }

    private void updateFilterButtonStyles() {
        setButtonActive(accessoriesButton, "Accessories".equalsIgnoreCase(currentCategory));
        setButtonActive(bagsButton, "Bags".equalsIgnoreCase(currentCategory));
        setButtonActive(clothingsButton, "Clothing".equalsIgnoreCase(currentCategory));
        setButtonActive(electronicsButton, "Electronics".equalsIgnoreCase(currentCategory));

        setButtonActive(dmfButton, "DMF".equalsIgnoreCase(currentLocation));
        setButtonActive(rsuButton, "RSU".equalsIgnoreCase(currentLocation));
        setButtonActive(huntButton, "Hunt".equalsIgnoreCase(currentLocation));
        setButtonActive(crimButton, "Crimson".equalsIgnoreCase(currentLocation));
        setButtonActive(maxwellButton, "Maxwell".equalsIgnoreCase(currentLocation));
        setButtonActive(burnellButton, "Burnell".equalsIgnoreCase(currentLocation));
    }

    private void setButtonActive(JButton button, boolean active) {
        Font current = button.getFont();

        if (active) {
            // Use FlatLaf's theme accent color, or fall back to a bright blue on 3rd-party black themes
            Color activeBg = UIManager.getColor("Component.accentColor");
            if (activeBg == null) {
                activeBg = new Color(37, 99, 235);
            }

            button.setBackground(activeBg);
            button.setForeground(Color.WHITE);
            button.setFont(current.deriveFont(Font.BOLD));
        } else {
            // Restore the FlatLaf dark theme's default button colors
            button.setBackground(UIManager.getColor("Button.background"));
            button.setForeground(UIManager.getColor("Button.foreground"));
            button.setFont(current.deriveFont(Font.PLAIN));
        }
    }

    public void setPosts(List<Post> posts) {
        // Initialization
        JLabel emptyLabel = new JLabel("No listings are available");
        emptyLabel.setFont(myFont);

        // Delete everything if there is anything, will be useful later with add/deleting
        listingsPanel.removeAll();
        postComps.clear();

        // If there are no posts, provide feedback. Will also be useful later
        if (posts.isEmpty()) {
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            listingsPanel.add(emptyLabel, constraints(0, 0, 1, 1,0, GridBagConstraints.HORIZONTAL, GridBagConstraints.PAGE_START));
        } else {
//            for (Post post : posts) {
//                // add all the new components. Constraints set on listing earlier forces organization
//                // listingsPanel.add(new PostComponent(post, controls));
//                PostComponent pc = new PostComponent(post,controls);
//                postComps.add(pc);
//                listingsPanel.add(pc);
//            }
            User currentUser = UserSession.getInstance().getCurrentUser();
            for(Post post: posts){
                PostComponent pc = new PostComponent(post, controls);

                // set the user for the view claimants button
                pc.setUser(currentUser);
                pc.setClaimButton();

                // add the comp
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

    /**
     * We add the posts only after the user has logged in
     */
    public void loadPosts() {
        this.allPosts = postService.findAll();
        setPosts(allPosts);
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

    // todo: update to work with userSession
    public void setUser(User user){
        // assign the current user
        this.user = user;

        // update the little components
        for(PostComponent pc: postComps){
            pc.setUser(this.user);
            pc.setClaimButton();
        }

        // build or rebuild depending on the user role
        addPostPanel.setVisible(user.isAdmin());

        headerPanel.revalidate();
        headerPanel.repaint();

        this.revalidate();
        this.repaint();
    }

    public User getUser(){
        return this.user;
    }

    public void refreshPosts(){
        makeSearch();
    }
    public void removePost(Post post) {
        allPosts.remove(post);
        makeSearch();
    }
}
