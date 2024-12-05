package com.example.dat_banh_fpoly.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private int categoryId;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double rating;
    private String sellerName;
    private String sellerPic;
    private int sellerTell;
    private ArrayList<String> size;
    private String title;

    // Constructor không thay đổi
    public Book() {}

    // Getter và Setter không thay đổi
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ArrayList<String> getPicUrl() { return picUrl; }
    public void setPicUrl(ArrayList<String> picUrl) { this.picUrl = picUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public String getSellerPic() { return sellerPic; }
    public void setSellerPic(String sellerPic) { this.sellerPic = sellerPic; }

    public int getSellerTell() { return sellerTell; }
    public void setSellerTell(int sellerTell) { this.sellerTell = sellerTell; }

    public ArrayList<String> getSize() { return size; }
    public void setSize(ArrayList<String> size) { this.size = size; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
