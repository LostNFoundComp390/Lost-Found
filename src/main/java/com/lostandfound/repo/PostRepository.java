package com.lostandfound.repo;

import com.lostandfound.model.Post;

import java.util.List;

public interface PostRepository {
    /**
     * @return a list with all the posts in the db
     */
    List<Post> findAll ();



}
