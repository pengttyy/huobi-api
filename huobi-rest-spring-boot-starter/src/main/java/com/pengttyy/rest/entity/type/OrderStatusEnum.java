package com.pengttyy.rest.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * 订单状态
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/15 16:47
 */
public enum OrderStatusEnum {
    PRE_SUBMITTED("pre-submitted", "准备提交"),
    SUBMITTED("submitted", "已提交"),
    PARTIAL_FILLED("partial-filled", "部分成交"),
    PARTIAL_CANCELED("partial-canceled", "部分成交撤消"),
    FILLED("filled", "完全成交"),
    CANCELED("canceled", "已撤消");

    private String code;
    private String desc;

    OrderStatusEnum(String code, String desc) {
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
