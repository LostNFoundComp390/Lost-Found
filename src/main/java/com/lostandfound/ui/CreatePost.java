package com.lostandfound.ui;

import com.lostandfound.model.Post;

import javax.swing.*;
import java.awt.*;

public class CreatePost extends JPanel{
    private  Font myFont = new Font("Tahoma", Font.PLAIN, 20);

    public static void test(){
        // Initialization
        JFrame frame = new JFrame("Create New Post Test");
        JPanel container = new JPanel(new GridBagLayout());

        // Container behavior
        container.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));


        // Frame behaviors
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(container);
        frame.setSize(450,350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CreatePost::test);}
}

