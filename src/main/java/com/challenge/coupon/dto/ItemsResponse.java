package com.challenge.coupon.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ItemsResponse {

    @ApiModelProperty(notes = "Lista de item_ids", required = true, example = "[\"MLA1\", \"MLA2\", \"MLA4\", \"MLA5\"]")
    @JsonProperty("item_ids")
    private List<String> itemIds;

    @ApiModelProperty(notes = "Monto total", required = true, example = "480")
    @JsonProperty("total")
    private double total;

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
