package com.pengttyy.rest.entity.account;

import lombok.Data;

/**
 * 账户信息
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 20:09
 */
@Data
public class Account {
    private long id;
    private String type;
    private String state;
    private String subtype;
}
