package com.challenge.coupon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.regex.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemsRequest {

    private static final java.util.regex.Pattern VALID_ITEM_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @ApiModelProperty(notes = "Lista de item_ids", required = true, example = "[\"MLA1\", \"MLA2\", \"MLA3\", \"MLA4\", \"MLA5\"]")
    @NotNull(message = "item_ids is required")
    @JsonProperty(value = "item_ids", required = true)
    private List<String> itemIds;

    @ApiModelProperty(notes = "Monto del cupón", required = true, example = "500")
    @NotNull(message = "amount is required")
    @Min(value = 1, message = "amount is not valid")
    @JsonProperty("amount")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Double amount;

    public ItemsRequest() {
    }

    public List<String> getItemIds() {
        return itemIds;
    }

    public Double getAmount() {
        return amount;
    }

    public static boolean validate(final String s) {
        return VALID_ITEM_ID_PATTERN.matcher(s).matches();
    }
}
