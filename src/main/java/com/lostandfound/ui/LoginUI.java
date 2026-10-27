package com.lostandfound.ui; /**
 * Kaitlyn Tucker
 * Comp390
 * Login screen without database functionality. Enter button doesn't work.
 */

import com.lostandfound.auth.UserAuthenticator;
import com.lostandfound.model.User;
import com.lostandfound.services.AuthService;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class LoginUI extends JPanel implements ActionListener {
    private UserAuthenticator userAuthenticator;
    private AuthService authService;
    private ControllerFrame controller;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel feedbackLabel;
    private JLabel titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI(ControllerFrame controller, AuthService authService) {
        this.userAuthenticator = userAuthenticator;
        this.authService = authService;
        this.controller = controller;
        buildPanel();
    }

    /*
     * Creates a new constraints object for each component so layout settings do not carry
     * over from one component to the next. Much easier to add them to panel
     */
    private GridBagConstraints constraints(
            int column,
            int row,
            int width,
            int fill,
            double weightx,
            int anchor
    ) {
        GridBagConstraints gbc = new GridBagConstraints();
        // Add 5 empty pixels as padding around every component
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = width;
        gbc.fill = fill; // determines if and where a component can expand into extra space
        gbc.weightx = weightx; // determines how components share extra space if multiple components can expand into the same space
        gbc.anchor = anchor; // determines component alignment within its own cell

        return gbc;
    }

    private void buildPanel() {
        // Changed this because I changed the whole class to be a Panel
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Login to Lst&Fnd!");

        usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(25); // spans 15 columns wide

        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(25);

        loginButton = new JButton("Log In");
        loginButton.addActionListener(this);

        // Start with a blank label that is visible but the bg is not opaque so the user can't see it
        // It still reserves space so that the window wont resize when the banner appears and disappears
        feedbackLabel = new JLabel(" ", SwingConstants.CENTER);
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setOpaque(false);

        // Title, login button, and feedback label span both columns.
        add(titleLabel, constraints(0, 0, 2, GridBagConstraints.NONE, 0, GridBagConstraints.CENTER));
        add(loginButton, constraints(0, 3, 2, GridBagConstraints.NONE, 0, GridBagConstraints.CENTER));
        add(feedbackLabel, constraints(0, 4, 2, GridBagConstraints.HORIZONTAL, 1, GridBagConstraints.CENTER));

        // The text fields consumes any extra horizontal space in their row
        add(usernameField, constraints(1, 1, 1, GridBagConstraints.HORIZONTAL, 1, GridBagConstraints.LINE_START));
        add(passwordField, constraints(1, 2, 1, GridBagConstraints.HORIZONTAL, 1, GridBagConstraints.LINE_START));

        // Rest of the components
        add(usernameLabel, constraints(0, 1, 1, GridBagConstraints.NONE, 0, GridBagConstraints.LINE_END));
        add(passwordLabel, constraints(0, 2, 1, GridBagConstraints.NONE, 0, GridBagConstraints.LINE_END));



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();

        // grab the password. But since it's swings password field it returns as an array of chars
        // we automatically convert it to a string to test
        String typedPassword = new String(passwordField.getPassword());

        // Authenticate users regardless of the data origin: file or database
        User user = authService.login(username, typedPassword);
        if (user != null) {
            // Display a success window
            feedbackLabel.setText("Login successful! :)");
            feedbackLabel.setBackground(Color.GREEN);
            feedbackLabel.setOpaque(true);

            usernameField.setText("");
            passwordField.setText("");
            feedbackTimer(1000);

            // this is where my BrowseListings page windows should open up
            // Switch to the right page
            controller.showBrowsing();

        } else {
            // Alert the user of their mistake
            feedbackLabel.setText("Username or password is incorrect!");
            feedbackLabel.setBackground(Color.RED);
            feedbackLabel.setOpaque(true);
            feedbackTimer(3000);
            passwordField.setText("");


        }

        // overwrite sensitive data
        typedPassword = "";
    }

    /*
     * Helper method to hide the feedback banner after some odd seconds
     */
    private void feedbackTimer(int milliseconds) {
        Timer timer = new Timer(milliseconds, new ActionListener() {
            public void actionPerformed(ActionEvent timerEvent) {
                feedbackLabel.setText(" ");
                feedbackLabel.setOpaque(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Used by {@link ControllerFrame} to set {@code loginButton} as the
     * frame's default button.
     * @return {@code JButton}
     */
    public JButton getLoginButton (){
        return loginButton;
    }
}