package com.lostandfound.repo;

import com.lostandfound.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * Check if username is already taken
     * @param username
     * @return true if the username is already taken, false otherwise
     */
    boolean exists(String username);

    /**
     * Returns the user record corresponding to the username
     * @param username
     * @return the user Object if found, and null otherwise
     */
    //TODO: return Optional<User> instead to avoid if(!null) block
    User findByUsername(String username);

    /**
     * Makes a new user if it doesn't already exist, otherwise it updates
     * the existing one
     * @param user
     */
    void save(User user);

    /**
     * Finds all the registered users
     * @return a list containing all User records; returns an empty list if no users are found
     */
    List<User> findAll();
}
