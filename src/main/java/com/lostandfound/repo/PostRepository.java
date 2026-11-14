package com.lostandfound.repo;

import com.lostandfound.model.Post;

import java.util.List;

public interface PostRepository {
    /**
     * Searches for all posts
     * @return {@link List}
     */
    List<Post> findAll ();

    /**
     * Save a new post into the database
     * @param post
     * @return the post id
     */
    long save(Post post);

    /**
     * Search the DB for a single post based on the ID
     * @param ID
     * @return {@link Post}
     */
    Post findById(long ID);

    /**
     * Searches the database for the requested category then returns a list of all the rows
     * @param category
     * @return {@link List<Post>} of post
     */
    List<Post> searchCategory(String category);

    List<Post> searchByKeywordNonExpired(String keyword);

    List<Post> searchByKeyword(String keyword);

    List<Post> findAllNonExpired();
}
