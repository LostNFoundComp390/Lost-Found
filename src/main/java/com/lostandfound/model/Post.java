package com.lostandfound.model;
import java.util.Map;

public class Post {
    // Initialization
    private String status;
    private String dateOfListing;
    private String desc;
    private String category;
    private String name;
    private String expirationDate;
    private int userID;
    private long productID;
    private String photo;
    private String valueStatus;
    private String location;

    // Implicit Constructor
    public Post(){
        this.status = null;
        this.dateOfListing = null;
        this.desc = null;
        this.category = null;
        this.name = null;
        this.expirationDate = null;
        this.userID = 0;
        this.productID = 0;
        this.photo= null;
        this.valueStatus = null;
        this.location = null;
    }

    // Explicit Constructor
    public Post(String status, String dateOfListing, String desc, String category, String name, String expirationDate, int userID, String photo,String valueStatus, String location){
        this.status = status;
        this.dateOfListing = dateOfListing;
        this.desc = desc;
        this.category = category;
        this.name = name;
        this.expirationDate = expirationDate;
        this.userID = userID;
        this.photo=photo;
        this.valueStatus = valueStatus;
        this.location = location;
    }

    // Constructor to handle the listed data
    private Post(Map<String, Object> row){
       // this.status = String)row.get("status"));
        this.status = (String) row.get("Status");
        this.dateOfListing = (String) row.get("DateOfListing");
        this.desc = (String) row.get("Description");
        this.category = (String) row.get("Category");
        this.name = (String) row.get("ProductName");
        this.expirationDate = (String) row.get("ExpirationDate");
        this.userID = (Integer) row.get("UserID");
        this.productID = (Integer) row.get("ProductID");
        this.photo= (String) row.get("Photo");
        this.valueStatus = (String) row.get("ValueStatus");
        this.location = (String) row.get("Location");
    }

    // Modified constructor from Bradley's user.java
    public static Post of(
            String status,
            String dateOfListing,
            String desc,
            String category,
            String name,
            String expirationDate,
            int userID,
            String photo,
            String valueStatus,
            String location)
    {
        return new Post(
                status,
                dateOfListing,
                desc,
                category,
                name,
                expirationDate,
                userID,
                photo,
                valueStatus,
                location);
    }

    // Modified static constructor
    public static Post fromRow(Map<String, Object> row) {
        return new Post(row);
    }

    // accessor methods
    public String getStatus(){
        return status;
    }
    public String getDateOfListing(){
        return dateOfListing;
    }
    public String getDesc(){
        return desc;
    }
    public String getCategory(){
        return category;
    }
    public String getName(){
        return name;
    }
    public String getExpirationDate(){
        return expirationDate;
    }
    public int getUserID(){
        return userID;
    }
    public long getProductID(){
        return productID;
    }
    public String getPhoto(){
        return photo;
    }
    public String getValueStatus(){
        return valueStatus;
    }
    public String getLocation(){
        return location;
    }

    // Mutators methods
    public void setStatus(String status){
        this.status = status;
    }
    public void setDateOfListing(String date){
        this.dateOfListing = date;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setExpirationDate(String date){
        this.expirationDate = date;
    }
    public void setUserID(int ID){
        this.userID = ID;
    }
    public void setProductID(long ID){
        this.productID = ID;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }
    public void setValueStatus(String value){
        this.valueStatus = value;
    }
    public void setLocation(String location){
        this.location = location;
    }

    // Override the toString function so I can read this data
    @Override
    public String toString() {
        return "Post{" +
                "status='" + status + '\'' +
                ", dateOfListing='" + dateOfListing + '\'' +
                ", desc='" + desc + '\'' +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", userID='" + userID + '\'' +
                ", productID=" + productID +
                ", photo='" + photo + '\'' +
                ", valueStatus='" + valueStatus + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
