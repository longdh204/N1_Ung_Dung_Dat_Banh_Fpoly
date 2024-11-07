package com.example.dat_banh_fpoly.Model;

import java.util.List;

public class OrderModel {
    private String userId;
    private String orderDate;
    private List<IteamsModel> listCart; // Giả sử listCart là danh sách các mặt hàng trong giỏ
    private double totalAmount;

    public OrderModel() {
    }

    // Constructor
    public OrderModel(String userId, String orderDate, List<IteamsModel> listCart, double totalAmount) {
        this.userId = userId;
        this.orderDate = orderDate;
        this.listCart = listCart;
        this.totalAmount = totalAmount;
    }

    // Getter và Setter nếu cần
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public List<IteamsModel> getListCart() {
        return listCart;
    }

    public void setListCart(List<IteamsModel> listCart) {
        this.listCart = listCart;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
