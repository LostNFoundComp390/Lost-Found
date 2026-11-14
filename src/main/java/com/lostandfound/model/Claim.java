package com.lostandfound.model;

public class Claim {
    private final long claimID;
    private final long postID;
    private final int userID;
    private final String username;
    private String status;

    public Claim(long claimID, long postID, int userID, String username, String status) {
        this.claimID = claimID;
        this.postID = postID;
        this.userID = userID;
        this.username = username;
        this.status = status;
    }

    public void setStatus(String status){
        this.status = status;
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
        if (status.equalsIgnoreCase("pending")){
            return username + " is currently awaiting your evaluation";
        }

        return username + " is currently " + status;

    }
}
