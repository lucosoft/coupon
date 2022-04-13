package com.challenge.coupon.dto;

public class Item {

    double price;
    String id;

    public Item(int price, String id) {
        this.price = price;
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

