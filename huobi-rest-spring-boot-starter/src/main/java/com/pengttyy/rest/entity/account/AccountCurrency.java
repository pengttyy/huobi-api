package com.pengttyy.rest.entity.account;/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 21:52
 */

import lombok.Data;

import java.util.List;

@Data
public class AccountCurrency {
    private String id;
    private String type;
    private String status;
    private List<Currency> list;
}
