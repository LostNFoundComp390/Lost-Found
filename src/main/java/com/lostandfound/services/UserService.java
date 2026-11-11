package com.lostandfound.services;

import com.lostandfound.exception.UserNotFoundException;
import com.lostandfound.model.User;
import com.lostandfound.model.UserSession;
import com.lostandfound.repo.UserRepository;
import com.password4j.Password;

/**
 * This class coordinates between the UI and the Repos.
 *
 * <p>Ui --> Services --> Repositories</p>
 *
 */
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo){
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

        if(username.isBlank() || password.isBlank()){return null;}
        
        User user = repo.findByUsername(username);
        if (user != null) {

            String hashFromDb = user.getPassword();
            verified = Password.check(password, hashFromDb).withBcrypt();
            UserSession.getInstance().setCurrentUser(user);
        }

        return verified ? user : null;
    }

    /**
     * Registers a new user in the system
     *
     * @param role
     * @param username
     * @param password
     * @param email
     * @throws RuntimeException if the user already exists
     */
    void register(String role, String username, String password, String email){

        if (!repo.exists(username)) {
            String hashedPassword = hashPassword(password);
            User newUser = User.of(
                role,
                username,
                hashedPassword,
                email
            );
            repo.save(newUser);
        }else {
            throw new RuntimeException("User already exists");
        }
    }

    /**
     * Deletes user from system if it exists
     *
     * @param username
     * @throws UserNotFoundException
     * @throws IllegalArgumentException if username is empty
     */
    void delete(String username) throws UserNotFoundException {
        if (username.isBlank()){throw new IllegalArgumentException("Username is empty");}
        if (!repo.exists(username)){throw new UserNotFoundException("User doesn't exist");}

        repo.delete(username);

    }



    private String hashPassword(String plaintext){
       return Password.hash(plaintext).withBcrypt().getResult();
    }
}
