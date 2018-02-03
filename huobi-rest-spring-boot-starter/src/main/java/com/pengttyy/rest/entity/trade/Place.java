package com.pengttyy.rest.entity.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 16:59
 */
@Data
public class Place {
    @JsonProperty("account-id")
    private String accountId;
    private String amount;
    private String price;
    private String source;
    private String symbol;
    private String type;
}
