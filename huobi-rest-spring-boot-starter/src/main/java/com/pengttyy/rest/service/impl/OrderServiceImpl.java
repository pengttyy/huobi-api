package com.pengttyy.rest.service.impl;/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 15:38
 */

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.client.Result;
import com.pengttyy.rest.entity.trade.OrderDetail;
import com.pengttyy.rest.entity.trade.Place;
import com.pengttyy.rest.entity.type.OrderStatusEnum;
import com.pengttyy.rest.service.IorderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.joining;

public class OrderServiceImpl implements IorderService {
    private static final String SIZE = "2";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HuobiRestTemplate restTemplate;

    @Override
    public Optional<OrderDetail> getOrderDetail(String orderId) {
        Result<OrderDetail> bean = this.restTemplate.getForBean("/v1/order/orders/{order-id}", new ParameterizedTypeReference<Result<OrderDetail>>() {
        }, orderId);
        return bean.getData();
    }

    @Override
    public List<OrderDetail> findOrders(String symbol, Set<OrderStatusEnum> status, int size) {
        String allStatus = status.stream().map(t -> t.getCode()).collect(joining(","));
        Result<List<OrderDetail>> listResult = this.restTemplate.getForList("/v1/order/orders?symbol={symbol}&states={states}&size={size}", new ParameterizedTypeReference<Result<List<OrderDetail>>>() {
        }, symbol, allStatus, size);
        logger.info("result msg{}", listResult.isSuccess());
        return listResult.getDataThrows();
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
