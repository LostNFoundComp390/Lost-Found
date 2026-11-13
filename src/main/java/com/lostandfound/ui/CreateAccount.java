package com.lostandfound.ui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class CreateAccount extends JFrame implements ActionListener {


    JTextField username = new JTextField();
    JPasswordField password = new JPasswordField(20);
    JPasswordField reenteredPassword = new JPasswordField(20);
    JTextField email = new JTextField();
    JTextField firstName = new JTextField();
    JTextField lastName = new JTextField();
    JButton createButton = new JButton();
    JLabel enterUsername = new JLabel("Enter desired username");
    JLabel enterPassword = new JLabel("Enter password");
    JLabel reenterPassword = new JLabel("Reenter password");
    JLabel enterEmail = new JLabel("Enter email");
    JLabel enterFirstName = new JLabel("Enter first name");
    JLabel enterLastName = new JLabel("Enter last name");

    private ControllerFrame controller;

    CreateAccount(ControllerFrame controller) {
        this.controller = controller;

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        createButton.addActionListener(this);
        createButton.setBounds(375, 400, 250, 25);
        createButton.setText("Create Account");
        createButton.setFocusable(false);
        createButton.setHorizontalTextPosition(JButton.CENTER);
        createButton.setVerticalTextPosition(JButton.BOTTOM);


        enterUsername.setBounds(350,25,300,25);
        enterUsername.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        username.setBounds(350, 50, 300, 25);
        username.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        username.setForeground(new Color(0, 0, 0));
        username.setBackground(Color.white);
        username.setCaretColor(Color.black);

        enterPassword.setBounds(350,75,300,25);
        enterPassword.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        password.setBounds(350, 100, 300, 25);
        password.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        password.setForeground(new Color(0, 0, 0));
        password.setBackground(Color.white);
        password.setCaretColor(Color.black);

        reenterPassword.setBounds(350,125,300,25);
        reenterPassword.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        reenteredPassword.setBounds(350, 150, 300, 25);
        reenteredPassword.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        reenteredPassword.setForeground(new Color(0, 0, 0));
        reenteredPassword.setBackground(Color.white);
        reenteredPassword.setCaretColor(Color.black);

        enterEmail.setBounds(350,175,300,25);
        enterEmail.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        email.setBounds(350, 200, 300, 25);
        email.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        email.setForeground(new Color(0, 0, 0));
        email.setBackground(Color.white);
        email.setCaretColor(Color.black);

        enterFirstName.setBounds(350,225,300,25);
        enterFirstName.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        firstName.setBounds(350, 250, 300, 25);
        firstName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        firstName.setForeground(new Color(0, 0, 0));
        firstName.setBackground(Color.white);
        firstName.setCaretColor(Color.black);

        enterLastName.setBounds(350,275,300,25);
        enterLastName.setFont(new Font("Times New Roman", Font.PLAIN, 15));

        lastName.setBounds(350, 300, 300, 25);
        lastName.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        lastName.setForeground(new Color(0, 0, 0));
        lastName.setBackground(Color.white);
        lastName.setCaretColor(Color.black);

        this.setResizable(false); //prevent frame from  being resized
        this.add(createButton);
        this.add(enterUsername);
        this.add(username);
        this.add(enterPassword);
        this.add(password);
        this.add(reenterPassword);
        this.add(reenteredPassword);
        this.add(enterEmail);
        this.add(email);
        this.add(enterFirstName);
        this.add(firstName);
        this.add(enterLastName);
        this.add(lastName);
        this.setTitle("Account Creation Page");
        this.setLayout(null);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton)
        {
            this.dispose();
            controller.showLogin();
            controller.setVisible(true);

        }
    }
}