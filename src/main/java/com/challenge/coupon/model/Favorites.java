package com.challenge.coupon.model;

import io.swagger.annotations.ApiModelProperty;

public class Favorites {

    @ApiModelProperty(notes = "item_ids", required = true, example = "MLA1")
    private String id;

    @ApiModelProperty(notes = "quantity", required = true, example = "2")
    private Long quantity;

    public Favorites() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
