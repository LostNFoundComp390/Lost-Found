package com.lostandfound.model;

public class User {
    private final String role;
    private final String username;
    private final String password;
    private final String email;
    private boolean isAdmin;

    private User(
        String role,
        String username,
        String password,
        String email) {

        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;

        if(this.role.equalsIgnoreCase("admin")){
            isAdmin = true;
        }else{
            isAdmin = false;
        }
    }

    // Static factory method
    public static User of(
        String role,
        String username,
        String password,
        String email) {

        return new User(role, username, password, email);

    }

    public static UserPublic toPublic (
        String role,
        String username,
        String email
    ){
       return UserPublic.of(role, username, email);
    }

        public String getUsername () {
            return username;
        }

        public String getPassword () {
            return password;
        }

        public String getEmail () {
            return email;
        }

        public String getRole () {
            return role;
        }
        public Boolean getAdmin(){return isAdmin;}
    }
