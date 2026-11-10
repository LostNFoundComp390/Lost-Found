package com.lostandfound.ui;
import com.lostandfound.db.Database;
import com.lostandfound.repo.DbUserRepository;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CreateAccount extends JFrame implements ActionListener {


    JTextField usernameField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();

    JPasswordField passwordField = new JPasswordField(20);
    JPasswordField confirmPasswordField = new JPasswordField(20);

    JButton registerButton = new JButton();

    JLabel enterUsernameLabel = new JLabel("Enter desired username");
    JLabel enterPasswordLabel = new JLabel("Enter password");
    JLabel confirmPasswordLabel = new JLabel("Confirm password");
    JLabel enterEmailLabel = new JLabel("Enter email");
    JLabel enterFirstNameLabel = new JLabel("Enter first name");
    JLabel enterLastNameLabel = new JLabel("Enter last name");
    JLabel passwordsMatchLabel = new JLabel("Passwords Match!");

    private ControllerFrame controller;

    private String username = "";
    private String password = "";
    private String reenteredPassword = "";
    private String email = "";
    private String firstName = "";
    private String lastName = "";

    private boolean usernameAvailable = false;
    private boolean legalPassword = false;
    private boolean passwordsMatch = false;
    private boolean emailAvailable = false;

    Database database = Database.production();
    DbUserRepository userRepo = new DbUserRepository(database);



    public CreateAccount(ControllerFrame controller) {
        this.controller = controller;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        registerButton.addActionListener(this);
        registerButton.setBounds(200, 400, 250, 40);
        registerButton.setText("Register Account");
        registerButton.setFocusable(false);
        registerButton.setHorizontalTextPosition(JButton.CENTER);
        registerButton.setVerticalTextPosition(JButton.BOTTOM);


        enterUsernameLabel.setBounds(175, 25, 300, 25);
        enterUsernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        usernameField.setBounds(175, 45, 300, 35);

        enterPasswordLabel.setBounds(175, 85, 300, 25);
        enterPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        passwordField.setBounds(175, 105, 300, 35);

        confirmPasswordLabel.setBounds(175, 145, 300, 25);
        confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        confirmPasswordField.setBounds(175, 165, 300, 35);

        enterEmailLabel.setBounds(175, 205, 300, 25);
        enterEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        emailField.setBounds(175, 225, 300, 35);

        enterFirstNameLabel.setBounds(175, 265, 300, 25);
        enterFirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        firstNameField.setBounds(175, 285, 300, 35);

        enterLastNameLabel.setBounds(175, 325, 300, 25);
        enterLastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        lastNameField.setBounds(175, 345, 300, 35);

        passwordsMatchLabel.setBounds(525, 165, 300, 25);
        passwordsMatchLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        this.setResizable(false); //prevent frame from  being resized
        this.add(registerButton);
        this.add(enterUsernameLabel);
        this.add(usernameField);
        this.add(enterPasswordLabel);
        this.add(passwordField);
        this.add(confirmPasswordLabel);
        this.add(confirmPasswordField);
        this.add(enterEmailLabel);
        this.add(emailField);
        this.add(enterFirstNameLabel);
        this.add(firstNameField);
        this.add(enterLastNameLabel);
        this.add(lastNameField);
        this.setTitle("Account Creation Page");
        this.setLayout(null);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        char[] passwordArray = passwordField.getPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');

        char[] reenteredPasswordArray = passwordField.getPassword();
        reenteredPassword = new String(reenteredPasswordArray);
        java.util.Arrays.fill(reenteredPasswordArray, ' ');

        email = emailField.getText();

        username = usernameField.getText();


    }//end CreateAccount

    private boolean doPasswordsMatch(String password, String reenteredPassword) {

        password = password;
        reenteredPassword = reenteredPassword;

        return password.equals(reenteredPassword);
    }//end doPasswordsMatch

    private boolean isPasswordLegal(String enteredPassword) {


        boolean isLegal = false;
        password = enteredPassword;

        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) != ' ') {
                isLegal = true;
            } else
                isLegal = false;
        }
        return isLegal;
    }//end isPasswordLegal

    private boolean isUsernameAvailable(String username )
    {
        username = username;
        if(userRepo.exists(username) == true)
        {
            usernameAvailable = false;
        }
        else
        {
            usernameAvailable = true;
        }
        return usernameAvailable;
    }//end isEmailAvailable


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
//            this.dispose();
//            controller.showLogin();
//            controller.setVisible(true);
//            if ((doPasswordsMatch(password, reenteredPassword) == true) && (isPasswordLegal(password) == true))
//            {
//
//
//            }//end inner if
            System.out.println(email);
        }//end outer if
    }//end actionPerformed
}
