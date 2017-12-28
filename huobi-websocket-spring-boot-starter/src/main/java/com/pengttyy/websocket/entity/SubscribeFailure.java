package com.pengttyy.websocket.entity;

import lombok.Data;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
@Data
public class SubscribeFailure {
    private String status;
    private String id;
    private String errCode;
    private String errMsg;
    private long ts;
}
