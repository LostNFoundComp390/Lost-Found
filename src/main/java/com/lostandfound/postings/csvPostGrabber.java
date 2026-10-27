package com.lostandfound.postings;


import com.lostandfound.model.Post;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

// Adding a CSV lib to handle long descriptions
// It's now a dependency in the pom.xml file

public class csvPostGrabber implements PostGrabber {
    // Initialization
    private final String filePath;
    public List<Post> posts = new ArrayList<>();

    public csvPostGrabber(String filePath) {
        this.filePath = filePath;
    }

    // Load posts into an array to use later
    @Override
    public List<Post> loadPosts() {
        // Initialization
        List<Post> posts = new ArrayList<>();

        // Look inside the posts file
        try (
                // Settings I found online to use this lib
                // Parses the filepath passed through
                Reader reader = new FileReader(filePath);
                CSVParser csvParser = CSVFormat.DEFAULT.builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .setTrim(true)
                        .get()
                        .parse(reader)
                )
        {
            // For all the lines inside the data file
            for(CSVRecord record: csvParser){
                // Parse the data and turn it into posts and add it to the array
                // Done more easily by skipping the hash map stuff
                posts.add(toPost(record));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    // Less annoying helper method
   private Post toPost(CSVRecord record){
       return Post.of(
               record.get("Status"),
               record.get("DateOfListing"),
               record.get("Description"),
               record.get("Category"),
               record.get("ProductName"),
               record.get("ExpirationDate"),
               record.get("UserID"),
               Integer.parseInt(record.get("ProductID")),
               record.get("Photo"),
               record.get("ValueStatus"),
               record.get("Location")
       );
   }


    // Test method to grab data
/*    public static void main(String[] args) {
        String filePath = "./data/LstAndFndListing.csv";

        csvPostGrabber grabber = new csvPostGrabber(filePath);

        List<Post> posts = grabber.loadPosts();

        System.out.println("Rows parsed: " + posts.size());

        for (int i = 0; i < posts.size(); i++) {
            System.out.println("Post " + (i + 1) + ": " + posts.get(i));
        }
    }*/
}
