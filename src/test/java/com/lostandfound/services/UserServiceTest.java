package com.lostandfound.services;

import com.lostandfound.exception.UserNotFoundException;
import com.lostandfound.model.User;
import com.lostandfound.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService userService;

    @Test
    void login_UserNotFound_ReturnsNull() {
        String username = "nonExistent";
        when(repo.findByUsername(username)).thenReturn(null);

        User result = userService.login(username, "anyPassword");

        assertNull(result);
    }
    @Test
    void login_BlankInputs_ReturnsNull() {
        // empty username
        assertNull(userService.login("", "password"), "login should return null when username is empty");

        // empty password
        assertNull(userService.login("user1", ""), "login should return null when username is empty");
    }

    @Test
    void delete_Success() throws UserNotFoundException {
        String username = "testUser";
        when(repo.exists(username)).thenReturn(true);

        userService.delete(username);

        verify(repo, times(1)).delete(username);
    }

    @Test
    void delete_EmptyUsername_ThrowsIllegalArgumentException() throws UserNotFoundException {
        String username = "";

        try {
            userService.delete(username);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Test passes because the correct exception was caught
            assertEquals("Username is empty", e.getMessage());
        }
    }

    @Test
    void delete_UserNotFound_ThrowsUserNotFoundException() {
        String username = "unknownUser";
        when(repo.exists(username)).thenReturn(false);

        try {
            userService.delete(username);
            fail("Expected UserNotFoundException was not thrown");
        } catch (UserNotFoundException e) {
            // Test passes because the correct exception was caught
            assertEquals("User doesn't exist", e.getMessage());
        }
    }
}
