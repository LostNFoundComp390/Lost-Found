package com.lostandfound;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.lostandfound.auth.CsvUserAuthenticator;
import com.lostandfound.auth.UserAuthenticator;
import com.lostandfound.db.Database;
import com.lostandfound.db.DatabaseInitializer;
import com.lostandfound.model.Post;
import com.lostandfound.repo.DbPostsRepository;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.repo.PostRepository;
import com.lostandfound.repo.UserRepository;
import com.lostandfound.services.AuthService;
import com.lostandfound.ui.ControllerFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Using invoke later because swing is thread unsafe
        setFont();
        FlatDarculaLaf.setup();
        SwingUtilities.invokeLater(() -> {
            Database db = null;
            // Initialize the database first
            try {
                db = Database.production();
                DatabaseInitializer.initialize(db);
                UserRepository userRepo = new DbUserRepository(db);
                PostRepository postRepo = new DbPostsRepository(db);
                AuthService authService = new AuthService(userRepo);

                // Initialize the posts list by using findAll method
                List<Post> posts = postRepo.findAll();

                try {
                    new ControllerFrame(authService, posts);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                // Send error back errors if failed, do not continue if DB cannot load
                e.printStackTrace();
                return;
            }

        });

    }

    // Set font to something more readable
    public static void setFont() {
        Font myFont = new Font("Tahoma", Font.PLAIN, 20);
        UIDefaults defaultUI = UIManager.getDefaults();
        defaultUI.put("Button.font", myFont);
        defaultUI.put("Label.font", myFont);
        defaultUI.put("ComboBox.font", myFont);
        defaultUI.put("TextArea.font", myFont);
    }
}
