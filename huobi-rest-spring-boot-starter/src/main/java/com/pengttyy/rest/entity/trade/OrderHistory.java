package com.pengttyy.rest.entity.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 已成功完成的订单
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 15:29
 */
@Data
public class OrderHistory {
    private long id;
    @JsonProperty("order-id")
    private long orderId;
    @JsonProperty("match-id")
    private long matchId;
    private String symbol;
    private String type;
    private String source;
    private String price;
    @JsonProperty("filled-amount")
    private String filledAmount;
    @JsonProperty("filled-fees")
    private String filledFees;
    @JsonProperty("created-at")
    private long createdAt;
}
