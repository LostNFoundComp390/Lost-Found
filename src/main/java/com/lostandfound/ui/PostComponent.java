package com.lostandfound.ui;

import com.lostandfound.model.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostComponent extends JPanel {
    // Main components
    private final int previewLength = 30;
    private  Font myFont = new Font("Tahoma", Font.PLAIN, 15);
    private Post post;
    private final JTextArea descriptionArea = new JTextArea();
    private final JButton more = new JButton("More...");
    private final JButton claim = new JButton("Yours?");
    private String description;
    private boolean expanded = false;

    public PostComponent(Post post){
        this.post  = post;
        this.description = post.getDesc();
        buildComponent();
    }

    private void buildComponent(){
        JLabel nameLabel = new JLabel(post.getName());
        JLabel locationLabel = new JLabel(post.getLocation());
        JLabel categoryLabel = new JLabel(post.getCategory());
        JLabel postedDate = new JLabel(("Found: "+ post.getPostedString()));
        JLabel expirationDate = new JLabel(("Expires: " + post.getStringExpiration()));

        JPanel descriptionContainer = new JPanel(new GridBagLayout());
        JPanel headerContainer = new JPanel(new GridBagLayout());
        JPanel buttonsContainer = new JPanel(new GridBagLayout());
        JPanel secondHeader = new JPanel(new GridBagLayout());

        setLayout(new GridBagLayout());
//        setBackground(Color.WHITE);

        // A compound border allows us to make an outline around the post card and add padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10,10,10,10)));

        // Container for images
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        imagePanel.setOpaque(false);

        String photoPath = post.getPhoto();
        if (photoPath != null && !photoPath.trim().isEmpty()) {
            // Split by comma to get each image filename
            String[] photos = photoPath.split(",");

            for (String photo : photos) {
                String cleanFileName = photo.trim();
                java.io.File imgFile = new java.io.File("images/" + cleanFileName);

                if (imgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(imgFile.getAbsolutePath());
                    Image scaledImg = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                    JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                    imagePanel.add(imgLabel);
                }
            }
        }
        // prep the header container
        secondHeader.add(categoryLabel, constraints(0,0,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_START));
        secondHeader.add(locationLabel,constraints(1,0,1,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.LINE_START));

        // prep the buttons container
        buttonsContainer.add(more,constraints(0,0,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_START));
        buttonsContainer.add(claim, constraints(1,0,1,1,GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START));

        // contain the description and the button
        // Put the button below the description
        descriptionContainer.add(descriptionArea, constraints(0,1,1,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.LINE_START));
        descriptionContainer.add(buttonsContainer,constraints(0,2,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_START));

        // contain the object name and the category next to each other
        headerContainer.add(nameLabel, constraints(0,0,1,0,GridBagConstraints.NONE, GridBagConstraints.LINE_START));
        // headerContainer.add(postedDate, constraints(1,0,1,1,GridBagConstraints.NONE, GridBagConstraints.LINE_START));
        headerContainer.add(expirationDate, constraints(2,0,1,1,GridBagConstraints.HORIZONTAL, GridBagConstraints.LINE_START));


        // The description area setup
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionArea.setColumns(16);
        descriptionArea.setText(shorten(description, previewLength));

        // assign fonts
        categoryLabel.setFont(myFont);
        nameLabel.setFont(myFont.deriveFont(Font.BOLD, 20f));
        locationLabel.setFont(myFont);
        descriptionArea.setFont(myFont);
        more.setFont(myFont);
        claim.setFont(myFont);
        expirationDate.setFont(myFont);
        postedDate.setFont(myFont);

        // assign a listener to the more button
        more.addActionListener(event -> toggleDesc());

        // assign a listener to the claim button
        claim.addActionListener(event -> toggleClaim());

        if(description.length() <= previewLength){
            more.setText(" ");
            more.setEnabled(false);
            more.setBorderPainted(false);
            more.setContentAreaFilled(false);
        }

        // Add the first header
        add(headerContainer,
                constraints(0,0,3,1,GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER));
        // Add the second header
        add(secondHeader,
                constraints(0,1,3,1,GridBagConstraints.NONE,GridBagConstraints.LINE_START));
        // Add the image panel
        add(imagePanel,
                constraints(0, 2, 1, 0, GridBagConstraints.NONE, GridBagConstraints.LINE_START));
        // Add the description
        add(descriptionContainer,
                constraints(1,2,1,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.LINE_END));
    }

    // Helper method stolen from another one of my projects
    private String shorten(String text, int maxLength){
        if(text.length()<=maxLength){
            return text;
        }
        return text.substring(0,maxLength) + "...";
    }

    private GridBagConstraints constraints(
            int column,
            int row,
            int width,
            float weightx,
            int fill,
            int anchor
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = width;
        gbc.fill = fill;
        gbc.weightx = weightx;
        gbc.anchor = anchor;

        return gbc;
    }

    private void toggleDesc(){
        setExpanded(!expanded);
    }
    private void toggleClaim(){post.setStatus("claim requested");}

    public void setExpanded(Boolean expanded){
        this.expanded = expanded;

        if(expanded){
            descriptionArea.setText(description);
            more.setText("Less...");
        }else{
            descriptionArea.setText(shorten(description,previewLength));
            more.setText("More...");
        }

        revalidate();
        repaint();
    }

    // Test display

    public static void test(){
        // Singular test post
        Post testPost = Post.of(
                "Unclaimed",
                "2026-09-28",
                "Black Hydroflask with a silver lid and a small dent near the bottom.",
                "Accessories",
                "Water Bottle",
                "2026-10-28",
                1,
                "hydroflask.jpg",
                "Standard",
                "DMF"
        );
        // singular frame just to display this component
        JFrame frame = new JFrame("Post Component Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panel made just to display this component and its parts
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
        container.add(new PostComponent(testPost), BorderLayout.CENTER);

        // Hard coded size, location is centering, and visibility is only required here
        // because there is no "main panel" which holds all the components. In the GUI the
        // main panel would be set to visible
        frame.setContentPane(container);
        frame.setSize(600, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
  

//    Test main
   public static void main(String[] args) {
       SwingUtilities.invokeLater(PostComponent::test);
   }
}
