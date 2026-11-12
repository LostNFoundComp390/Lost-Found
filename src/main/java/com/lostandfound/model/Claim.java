package com.lostandfound.model;

public class Claim {
    private final long claimID;
    private final int userID;
    private final String username;
    private final String status;

    public Claim(long claimID, int userID, String username, String s) {
        this.claimID = claimID;
        this.userID = userID;
        this.username = username;
        status = s;
    }

    public long getClaimID(){
        return claimID;
    }
    public int getUserID(){
        return userID;
    }
    public String getUsername(){
        return username;
    }
    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return username + " " + status;
    }
}
