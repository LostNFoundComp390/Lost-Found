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

    public User login(String username, String password) {
        boolean verified = false;

        if (repo.exists(username)) {

            Hash hash = Password.hash(password).addRandomSalt().withArgon2();
            String hashFromDb = repo.findByUsername(username).getPassword();

            verified = Password.check(hash.getResult(), hashFromDb).withArgon2();
        }

        return verified ? repo.findByUsername(username) : null;
    }

    void register(String username, String password){

        //TODO: register new users with repo.save(newUser);
    }
}
