package com.lostandfound.model;

public class UserPublic {
    private final String role;
    private final String username;
    private final String email;

    private UserPublic(
            String role,
            String username,
            String email) {

        this.role = role;
        this.username = username;
        this.email = email;
    }

    // Static factory method
    public static UserPublic of(
            String role,
            String username,
            String email) {

        return new UserPublic(role, username, email);

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
