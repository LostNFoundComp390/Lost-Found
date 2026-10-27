package com.lostandfound;

import com.lostandfound.auth.CsvUserAuthenticator;
import com.lostandfound.auth.UserAuthenticator;
import com.lostandfound.db.Database;
import com.lostandfound.db.DatabaseInitializer;
import com.lostandfound.model.Post;
import com.lostandfound.postings.PostGrabber;
import com.lostandfound.postings.csvPostGrabber;
import com.lostandfound.ui.ControllerFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Main {
    /*
     * Swing is not thread safe. Oracle suggests using invokeLater because it wont block your main thread
     * while you are waiting for your GUI to build. Idk if Dr. Kumari prefers this or new LoginUIClean();
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setFont();

            // Initialize

            UserAuthenticator userAuthenticator = new CsvUserAuthenticator("src/main/resources/UserAccounts.csv");
            PostGrabber postGrabber = new csvPostGrabber("src/main/resources/LstAndFndListing.csv");

            List<Post> posts = postGrabber.loadPosts();
            try {
                new ControllerFrame(userAuthenticator, posts);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            Database db = Database.production();
            DatabaseInitializer.initialize(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set font to something more readable
    public static void setFont(){
        Font myFont = new Font("Tahoma", Font.PLAIN, 20);
        UIDefaults defaultUI = UIManager.getDefaults();
        defaultUI.put("Button.font", myFont);
        defaultUI.put("Label.font", myFont);
        defaultUI.put("ComboBox.font", myFont);
        defaultUI.put("TextArea.font", myFont);
    }
}
