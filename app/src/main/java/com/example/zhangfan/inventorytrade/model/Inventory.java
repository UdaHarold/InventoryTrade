package com.example.zhangfan.inventorytrade.model;

/**
 * Created by Harold on 2017/9/4.
 */

public class Inventory {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private int saleTimes;
    private String imagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSaleTimes() {
        return saleTimes;
    }

    public void setSaleTimes(int saleTimes) {
        this.saleTimes = saleTimes;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
