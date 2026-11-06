package com.lostandfound.ui;

import com.lostandfound.model.Post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PostComponent extends JPanel {
    // Main components
    private final int previewLength = 30;
    private  Font myFont = new Font("Tahoma", Font.PLAIN, 20);
    private Post post;
    private final JTextArea descriptionArea = new JTextArea();
    private final JButton more = new JButton("More...");
    private String description;
    private boolean expanded = false;

    public PostComponent(Post post){
        this.post  = post;
        this.description = post.getDesc();
        buildComponent();
    }

    private void buildComponent(){
        setLayout(new GridBagLayout());
//        setBackground(Color.WHITE);

        // A compound border allows us to make an outline around the post card and add padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10,10,10,10)));

        // --- MULTIPLE IMAGES CONTAINER ---
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

        // Add the image panel at top left (column 0, row 0)
        add(imagePanel, constraints(0, 0, 1, 0, GridBagConstraints.NONE, GridBagConstraints.LINE_START));

        JLabel nameLabel = new JLabel(post.getName());
        JLabel locationLabel = new JLabel(post.getLocation());
        JLabel categoryLabel = new JLabel(post.getCategory());

        // The description area setup
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder());
        descriptionArea.setColumns(16);
        descriptionArea.setText(shorten(description, previewLength));

        // assign fonts
        nameLabel.setFont(myFont.deriveFont(Font.BOLD, 16f));
        locationLabel.setFont(myFont);
        descriptionArea.setFont(myFont);
        more.setFont(myFont);

        // assign a listener to the more button
        more.addActionListener(event -> toggleDesc());

        if(description.length() <= previewLength){
            more.setText(" ");
            more.setEnabled(false);
            more.setBorderPainted(false);
            more.setContentAreaFilled(false);
        }

        // add Name next to images
        add(nameLabel,
                constraints(1,0,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_START));
        // add category next to name
        add(categoryLabel,
                constraints(2,0,1,0,GridBagConstraints.NONE,GridBagConstraints.LINE_START));
        // add description below
        add(descriptionArea,
                constraints(0,1,3,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.CENTER));
        add(locationLabel,
                constraints(0,2,1,0, GridBagConstraints.NONE, GridBagConstraints.LINE_START));
        add(more,
                constraints(0,3,1,0,GridBagConstraints.NONE, GridBagConstraints.LINE_START));
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
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
  
//    Test main
   public static void main(String[] args) {
       SwingUtilities.invokeLater(PostComponent::test);
   }
}
