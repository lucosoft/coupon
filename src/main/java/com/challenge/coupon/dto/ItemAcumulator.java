package com.challenge.coupon.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ItemAcumulator {

    @JsonProperty("amount")
    double amount;

    @JsonIgnore
    double amountMax;

    @JsonProperty("item_ids")
    List<Item> items;

    public ItemAcumulator(double amountMax) {
        this.amountMax = amountMax;
        this.items = new ArrayList<>();
    }

    public void add(Item item) {
        if(item.getPrice() + this.amount < this.amountMax){
            items.add(item);
            computeRating();
        }
    }

    private double computeRating() {

        double totalPoints = items.stream().map(Item::getPrice).reduce(0.0, Double::sum);
        this.amount = totalPoints;
        return this.amount;
    }

    public double getAmount() {
        return amount;
    }

    public List<Item> getItems() {
        return items;
    }
}
