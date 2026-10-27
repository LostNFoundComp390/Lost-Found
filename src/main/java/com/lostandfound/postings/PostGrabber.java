package com.lostandfound.postings;

import com.lostandfound.model.Post;
import java.util.List;


public interface PostGrabber {
    List<Post> loadPosts();
}
