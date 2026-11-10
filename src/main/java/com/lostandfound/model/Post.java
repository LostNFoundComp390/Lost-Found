package com.lostandfound.model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Post {
    // Initialization
    private String status;
    private LocalDate dateOfListing;
    private String desc;
    private String category;
    private String name;
    private LocalDate expirationDate;
    private int ownerID;
    private int claimerID;
    private long productID;
    private String photo;
    private String valueStatus;
    private String location;
    private final DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    // Implicit Constructor
    public Post(){
        this.status = null;
        this.dateOfListing = null;
        this.desc = null;
        this.category = null;
        this.name = null;
        this.expirationDate = null;
        this.ownerID = 0;
        this.claimerID =0;
        this.productID = 0;
        this.photo= null;
        this.valueStatus = null;
        this.location = null;
    }

    // Explicit Constructor w/o a claimant
    public Post(String status, String dateOfListing, String desc, String category, String name, String expirationDate, int ownerID, String photo,String valueStatus, String location){
        this.status = status;
        this.dateOfListing = LocalDate.parse(dateOfListing,datePattern);
        this.desc = desc;
        this.category = category;
        this.name = name;
        this.expirationDate = LocalDate.parse(expirationDate, datePattern);
        this.ownerID = ownerID;
        this.claimerID = 0;
        this.photo=photo;
        this.valueStatus = valueStatus;
        this.location = location;
    }

    // Explicit Constructor w/ a claimant
    public Post(String status, String dateOfListing, String desc, String category, String name, String expirationDate, int ownerID, int claimerID, String photo,String valueStatus, String location){
        this.status = status;
        this.dateOfListing = LocalDate.parse(dateOfListing,datePattern);
        this.desc = desc;
        this.category = category;
        this.name = name;
        this.expirationDate = LocalDate.parse(expirationDate, datePattern);
        this.ownerID = ownerID;
        this.claimerID = claimerID;
        this.photo=photo;
        this.valueStatus = valueStatus;
        this.location = location;
    }


    // Constructor to handle the listed data
    private Post(Map<String, Object> row){
       // this.status = String)row.get("status"));
        this.status = (String) row.get("Status");
        this.dateOfListing = LocalDate.parse ((String) row.get("DateOfListing"), datePattern);
        this.desc = (String) row.get("Description");
        this.category = (String) row.get("Category");
        this.name = (String) row.get("ProductName");
        this.expirationDate = LocalDate.parse ((String) row.get("ExpirationDate"), datePattern);
        this.ownerID = (Integer) row.get("UserID");
        this.claimerID = (Integer) row.get("claimerID");
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
    public LocalDate getDateOfListing(){
        return dateOfListing;
    }
    public String getPostedString(){
        return dateOfListing.format(datePattern);
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
    public LocalDate getExpirationDate(){
        return expirationDate;
    }
    public String getStringExpiration(){
        return expirationDate.format(datePattern);
    }
    public int getOwnerID(){
        return ownerID;
    }
    public int getClaimerID(){ return claimerID; }
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
    public void setDateOfListing(String date){this.dateOfListing = LocalDate.parse(date,datePattern);}
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
        this.expirationDate = LocalDate.parse(date,datePattern);
    }
    public void setownerID(int ID){
        this.ownerID = ID;
    }
    public void setClaimerID(int ID){this.claimerID = ID;}
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
                ", userID='" + ownerID + '\'' +
                ", productID=" + productID +
                ", photo='" + photo + '\'' +
                ", valueStatus='" + valueStatus + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
