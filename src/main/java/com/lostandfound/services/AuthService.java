package com.lostandfound.services;

import com.lostandfound.model.User;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.repo.UserRepository;
import com.password4j.Hash;
import com.password4j.Password;

/**
 * This class coordinates between the UI and the Repos.
 *
 * <p>Ui --> Services --> Repositories</p>
 *
 */
public class AuthService {

    private final UserRepository repo;

    public AuthService (UserRepository repo){
       this.repo = repo;
    }

    /**
     * Verifies the user logic credential is valid by checking
     * that the username exists, and that the plain text password
     * will match the hashed password stored when hashed.
     * @param username
     * @param password
     * @return A {@link User} if credentials match, else {@code null}
     */
    public User login(String username, String password) {
        boolean verified = false;

        User user = repo.findByUsername(username);
        if (user != null) {

            // TODO: needs to return the hashpassword in db, not user object password
            String hashFromDb = user.getPassword();
            verified = Password.check(password, hashFromDb).withBcrypt();
        }

        return verified ? repo.findByUsername(username) : null;
    }

    void register(String role, String username, String password, String email){

        //TODO: register new users with repo.save(newUser);
    }
}
