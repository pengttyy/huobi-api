package com.pengttyy.rest.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/15 17:39
 */
public enum TradeTypeEnum {
    BUY_MARKET("buy-market", "市价买"),
    SELL_MARKET("sell-market", "市价卖"),
    BUY_LIMIT("buy-limit", "限价买"),
    SELL_LIMIT("sell-limit", "限价卖");
    private String code;
    private String desc;

    TradeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @JsonCreator
    public static OrderStatusEnum getItem(String code) {
        return Arrays.stream(OrderStatusEnum.values())
                .filter(item -> item.getCode().equals(code))
                .findFirst().orElseThrow(() -> new NullPointerException("不存在此状态的类型" + code));
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
