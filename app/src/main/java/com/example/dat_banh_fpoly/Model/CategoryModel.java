package com.example.dat_banh_fpoly.Model;

public class CategoryModel {
    private String title; // 3 usages
    private int id; // 3 usages
    private String picUrl; // 3 usages

    public CategoryModel() {
        // no usages
    }

    public CategoryModel(String title, int id, String picUrl) { // no usages
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
