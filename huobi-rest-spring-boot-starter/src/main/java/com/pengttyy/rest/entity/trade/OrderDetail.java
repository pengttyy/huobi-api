package com.pengttyy.rest.entity.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pengttyy.rest.entity.type.OrderStatusEnum;
import com.pengttyy.rest.entity.type.TradeTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 21:24
 */
@Data
public class OrderDetail {
    private long id;
    private String symbol;
    @JsonProperty("account-id")
    private String accountId;
    private BigDecimal amount;

    private BigDecimal price;
    @JsonProperty("created-at")
    private Date createdAt;

    private TradeTypeEnum type;

    @JsonProperty("field-amount")
    private BigDecimal fieldAmount;
    @JsonProperty("field-cash-amount")
    private BigDecimal fieldCashAmount;

    @JsonProperty("field-fees")
    private BigDecimal fieldFees;

    @JsonProperty("finished-at")
    private Date finishedAt;

    private String source;
    private OrderStatusEnum state;
    @JsonProperty("canceled-at")
    private Date canceledAt;


    /**
     * 格式化的创建时间
     *
     * @return
     */
    public String getCreatedAtStr() {
        return DateFormatUtils.format(this.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化的完成时间
     *
     * @return
     */
    public String getFinishedAtStr() {
        return DateFormatUtils.format(this.getFinishedAt(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化的取消时间
     *
     * @return
     */
    public String getCanceledAtStr() {
        return DateFormatUtils.format(this.getCanceledAt(), "yyyy-MM-dd HH:mm:ss");
    }


}
