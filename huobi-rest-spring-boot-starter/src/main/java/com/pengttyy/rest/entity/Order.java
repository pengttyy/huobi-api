package com.pengttyy.rest.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 21:24
 */
@Data
public class Order {
    private long id;
    private String symbol;
    @JsonProperty("account-id")
    private String accountId;
    private String amount;

    private String price;
    @JsonProperty("created-at")
    private long createdAt;

    private String type;

    @JsonProperty("field-amount")
    private String fieldAmount;
    @JsonProperty("field-cash-amount")
    private String fieldCashAmount;

    @JsonProperty("field-fees")
    private String fieldFees;

    @JsonProperty("finished-at")
    private long finishedAt;

    private String source;
    private String state;
    @JsonProperty("canceled-at")
    private long canceledAt;
}
