package com.lostandfound.ui;
import com.lostandfound.db.Database;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.repo.UserRepository;
import com.lostandfound.services.FieldChecks;
import com.lostandfound.services.UserService;

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

    JLabel enterUsernameLabel = new JLabel("Enter desired username.");
    JLabel enterPasswordLabel = new JLabel("Enter password. Must be 8-20 characters.");
    JLabel confirmPasswordLabel = new JLabel("Confirm password.");
    JLabel enterEmailLabel = new JLabel("Enter your student email.");
    JLabel enterFirstNameLabel = new JLabel("Enter first name.");
    JLabel enterLastNameLabel = new JLabel("Enter last name.");

    JLabel usernameFieldEmpty = new JLabel("Please enter a usable username");
    JLabel usernameUnavailable = new JLabel("This username is in use. Please try again.");
    JLabel usernameTooSmall = new JLabel("Username must be longer than 4 characters");
    JLabel passwordFieldEmpty = new JLabel("Please enter a password");
    JLabel passwordsDontMatch = new JLabel("Passwords do not match. Please try again.");
    JLabel illegalPassword = new JLabel("Passwords cannot contain spaces. Please try again.");
    JLabel passwordTooShort = new JLabel("Password is not long enough. Try again.");
    JLabel emailTextFieldEmpty = new JLabel("No email address was entered.");
    JLabel emailInUse = new JLabel("Email entered is associated with another account. Please enter new email");
    JLabel illegalEmail = new JLabel("Incorrect email syntax. Please enter a valid email.");
    JLabel firstNameTextFieldEmpty = new JLabel("Please enter your first name.");
    JLabel lastNameTextFieldEmpty = new JLabel("Please enter your last name.");

    private ControllerFrame controller;

    private String username = "";
    private String password = "";
    private String reenteredPassword = "";
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String user = "user;";

    private boolean isUsernameFieldEmpty = false;
    private boolean isUsernameAvailable = false;
    private boolean isUsernameMinimumLength = false;
    private boolean isPasswordFieldEmpty = true;
    private boolean doPasswordsMatch = false;
    private boolean isPasswordLegal = false;
    private boolean isPasswordTooShort = false;
    private boolean isEmailTextFieldEmpty = true;
    private boolean isEmailSyntaxCorrect = false;
    private boolean isFirstNameFieldEmpty = true;
    private boolean isLastNameFieldEmpty = true;


    Database database = Database.production();
    DbUserRepository userRepo = new DbUserRepository(database);
    FieldChecks fieldChecks = new FieldChecks();
    UserRepository repo;
    UserService userService = new UserService(repo);

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

        usernameFieldEmpty.setBounds(525, 45, 500, 35);
        usernameFieldEmpty.setVisible(false);

        usernameUnavailable.setBounds(525, 45, 500, 25);
        usernameUnavailable.setVisible(false);

        usernameTooSmall.setBounds(525, 45, 400, 35);
        usernameTooSmall.setVisible(false);

        enterPasswordLabel.setBounds(175, 85, 300, 25);
        enterPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        passwordField.setBounds(175, 105, 300, 35);

        passwordFieldEmpty.setBounds(525, 105, 400, 35);
        passwordFieldEmpty.setVisible(false);

        passwordsDontMatch.setBounds(525, 105, 400, 35);
        passwordsDontMatch.setVisible(false);

        illegalPassword.setBounds(525, 105, 500, 35);
        illegalPassword.setVisible(false);

        passwordTooShort.setBounds(525, 105, 500,35);
        passwordTooShort.setVisible(false);

        confirmPasswordLabel.setBounds(175, 145, 300, 25);
        confirmPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        confirmPasswordField.setBounds(175, 165, 300, 35);

        enterEmailLabel.setBounds(175, 205, 300, 25);
        enterEmailLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        emailField.setBounds(175, 225, 300, 35);

        emailTextFieldEmpty.setBounds(525, 225, 300, 35);
        emailTextFieldEmpty.setVisible(false);

        illegalEmail.setBounds(525, 225, 400, 35);
        illegalEmail.setVisible(false);

        enterFirstNameLabel.setBounds(175, 265, 300, 25);
        enterFirstNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        firstNameField.setBounds(175, 285, 300, 35);
        firstNameTextFieldEmpty.setBounds(525, 285, 300, 35);
        enterLastNameLabel.setBounds(175, 325, 300, 25);
        enterLastNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));

        lastNameField.setBounds(175, 345, 300, 35);
        lastNameTextFieldEmpty.setBounds(525, 345, 300, 35);

        this.setResizable(false); //prevent frame from  being resized
        this.add(registerButton);
        this.add(enterUsernameLabel);
        this.add(usernameFieldEmpty);
        this.add(usernameUnavailable);
        this.add(usernameTooSmall);
        this.add(usernameField);
        this.add(enterPasswordLabel);
        this.add(passwordField);
        this.add(passwordFieldEmpty);
        this.add(passwordTooShort);
        this.add(confirmPasswordLabel);
        this.add(confirmPasswordField);
        this.add(passwordsDontMatch);
        this.add(illegalPassword);
        this.add(enterEmailLabel);
        this.add(emailField);
        this.add(emailTextFieldEmpty);
        this.add(illegalEmail);
        this.add(enterFirstNameLabel);
        this.add(firstNameField);
        this.add(enterLastNameLabel);
        this.add(lastNameField);
        this.setTitle("Account Creation Page");
        this.setLayout(null);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }//end CreateAccount

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            this.setVisible(true);

            username = usernameField.getText();
            email = emailField.getText();
            firstName = firstNameField.getText();
            lastName = lastNameField.getText();

            char[] passwordArray = passwordField.getPassword();
            password = new String(passwordArray);
            java.util.Arrays.fill(passwordArray, ' ');

            char[] reenteredPasswordArray = confirmPasswordField.getPassword();
            reenteredPassword = new String(reenteredPasswordArray);
            java.util.Arrays.fill(reenteredPasswordArray, ' ');

            isUsernameFieldEmpty = fieldChecks.isUsernameFieldEmpty(username);
            isUsernameAvailable = fieldChecks.isUsernameAvailable(username);
            isUsernameMinimumLength = fieldChecks.isUsernameMinimumLength(username);

            isPasswordFieldEmpty = fieldChecks.isPasswordFieldEmpty(password);
            doPasswordsMatch = fieldChecks.doPasswordsMatch(password, reenteredPassword);
            isPasswordLegal = fieldChecks.isPasswordLegal(password);
            isPasswordTooShort = fieldChecks.passwordLength(password);

            isEmailTextFieldEmpty = fieldChecks.isEmailTextFieldEmpty(email);
           // isEmailSyntaxCorrect = fieldChecks.isEmailSyntaxCorrect(email);

            isFirstNameFieldEmpty = fieldChecks.isFirstNameFieldEmpty(firstName);
            isLastNameFieldEmpty = fieldChecks.isLastNameFieldEmpty(lastName);

            if(isUsernameFieldEmpty)
            {
                usernameFieldEmpty.setVisible(true);
                usernameUnavailable.setVisible(false);
                usernameTooSmall.setVisible(false);

            }
            else if(!isUsernameAvailable)
            {
                usernameUnavailable.setVisible(true);
                usernameFieldEmpty.setVisible(false);
                usernameTooSmall.setVisible(false);
            }
            else if(!isUsernameMinimumLength)
             {
                 usernameTooSmall.setVisible(true);
                 usernameUnavailable.setVisible(false);
                 usernameFieldEmpty.setVisible(false);
             }
            else
            {
                usernameTooSmall.setVisible(false);
                usernameUnavailable.setVisible(false);
                usernameFieldEmpty.setVisible(false);
            }

            if(isPasswordFieldEmpty)
            {
                passwordFieldEmpty.setVisible(true);
                passwordsDontMatch.setVisible(false);
                illegalPassword.setVisible(false);
                passwordTooShort.setVisible(false);
            }
            else if(!doPasswordsMatch)
            {
                passwordFieldEmpty.setVisible(false);
                passwordsDontMatch.setVisible(true);
                illegalPassword.setVisible(false);
                passwordTooShort.setVisible(false);
            }
            else if(!isPasswordLegal)
            {
                passwordFieldEmpty.setVisible(false);
                passwordsDontMatch.setVisible(false);
                illegalPassword.setVisible(true);
                passwordTooShort.setVisible(false);
            }
            else if (!isPasswordTooShort)
            {
                passwordFieldEmpty.setVisible(false);
                passwordsDontMatch.setVisible(false);
                illegalPassword.setVisible(false);
                passwordTooShort.setVisible(true);
            }
            else
            {
                passwordFieldEmpty.setVisible(false);
                passwordsDontMatch.setVisible(false);
                illegalPassword.setVisible(false);
                passwordTooShort.setVisible(false);
            }

             if(isEmailTextFieldEmpty)
             {
                 emailTextFieldEmpty.setVisible(true);
                 illegalEmail.setVisible(false);

             }
//             else if(!isEmailSyntaxCorrect)
//             {
//                 emailTextFieldEmpty.setVisible(false);
//                 illegalEmail.setVisible(true);
//             }
//             else
//             {
//                 emailTextFieldEmpty.setVisible(false);
//                 illegalEmail.setVisible(false);
//             }

            if (isFirstNameFieldEmpty) {
                 this.setVisible(true);
                 this.add(firstNameTextFieldEmpty);
             }
             else {
                        firstNameTextFieldEmpty.setVisible(false);
                    }

             if (isLastNameFieldEmpty) {
                 this.setVisible(true);
                 this.add(lastNameTextFieldEmpty);
             }
             else {
                        lastNameTextFieldEmpty.setVisible(false);
                    }

             if (!isUsernameFieldEmpty && isUsernameAvailable && isUsernameMinimumLength && !isPasswordFieldEmpty && doPasswordsMatch && isPasswordLegal && !isEmailTextFieldEmpty && !isFirstNameFieldEmpty && !isLastNameFieldEmpty) {
                        userService.register(user, username, password, email);
                    }
                }//end if(e.getSource() == registerButton)
            }//end actionPerformed
        }

