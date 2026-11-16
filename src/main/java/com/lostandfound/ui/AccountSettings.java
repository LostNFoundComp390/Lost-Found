package com.lostandfound.ui;
import com.lostandfound.db.Database;
import com.lostandfound.model.User;
import com.lostandfound.model.UserPublic;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.repo.UserRepository;
import com.lostandfound.services.FieldChecks;
import com.lostandfound.services.UserService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AccountSettings extends JFrame   {

    private ControllerFrame accountSettings;

    private String firstName;
    private String lastName;
    private String username;
    private String email;


    private JLabel firstNameLabel = new JLabel("First Name: ");
    private JLabel lastNameLabel = new JLabel("Last Name: ");
    private JLabel usernameLabel = new JLabel("Username: ");
    private JLabel emailLabel = new JLabel("Email: ");


    public AccountSettings(ControllerFrame controller) {

        this.accountSettings = controller;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        firstNameLabel.setBounds(100,100, 300,35);
        lastNameLabel.setBounds(100,200, 300,35);
        usernameLabel.setBounds(100,300, 300,35);
        emailLabel.setBounds(100,400, 300,35);

        this.setTitle("Account Creation Page");
        this.setLayout(null);
        this.setSize(1000, 1000);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.add(firstNameLabel);
        this.add(lastNameLabel);
        this.add(usernameLabel);
        this.add(emailLabel);

    }








}
