package com.pengttyy.rest.service.impl;/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 15:38
 */

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.entity.Place;
import com.pengttyy.rest.entity.Result;
import com.pengttyy.rest.entity.trade.OrderHistory;
import com.pengttyy.rest.service.IorderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements IorderService {
    private static final String SIZE = "1";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HuobiRestTemplate restTemplate;

    @Override
    public Optional<OrderHistory> findLatestOrderHistory(String symbol) {
        Result<List<OrderHistory>> orderHistoryResult = this.restTemplate.getForList("/v1/order/matchresults?symbol={symbol}&size={size}",
                new ParameterizedTypeReference<Result<List<OrderHistory>>>() {
                }, symbol, SIZE);
        Optional<OrderHistory> first = orderHistoryResult.getData().get().stream().findFirst();
        return first;
    }

    @Override
    public Optional<String> createOrder(Place place) {
        Result<String> stringResult = this.restTemplate.postForBean("/v1/order/orders/place", place, new ParameterizedTypeReference<Result<String>>() {
        });
        return stringResult.getData();
    }

    @Override
    public Optional<String> submitcancel(String orderId) {
        Result<String> stringResult = this.restTemplate.postForBean("/v1/order/orders/{order-id}/submitcancel", null, new ParameterizedTypeReference<Result<String>>() {
        }, orderId);
        return stringResult.getData();
    }
}
