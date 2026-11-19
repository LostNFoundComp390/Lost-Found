package com.lostandfound.model;

public class User {
    private final String role;
    private final String username;
    private final String password;
    private final String email;
    private boolean isAdmin;
    private int userID;

    private User(
        int userID,
        String role,
        String username,
        String password,
        String email) {

        this.userID = userID;
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

        return new User(0, role, username, password, email);
    }

    // used for db
    public static User of(
            int userID,
            String role,
            String username,
            String password,
            String email) {

        return new User(userID, role, username, password, email);
    }


    public static UserPublic toPublic(
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
        public Boolean isAdmin(){return isAdmin;}
        public int getUserID() {return userID;}
}
