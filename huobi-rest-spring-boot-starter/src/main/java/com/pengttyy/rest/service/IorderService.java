package com.pengttyy.rest.service;

import com.pengttyy.rest.entity.trade.OrderDetail;
import com.pengttyy.rest.entity.trade.Place;
import com.pengttyy.rest.entity.type.OrderStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 交易订单相关服务接口
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 15:57
 */
public interface IorderService {
    /**
     * 查询最后一次订单成交记录
     *
     * @param orderId 订单id
     * @return 最后一次交易成功结果
     */
    Optional<OrderDetail> getOrderDetail(String orderId);


    /**
     * 查询历史订单信息
     *
     * @param symbol 交易对
     * @param status 状态枚举
     * @param size   查询记录大小
     * @return 订单信息集合
     */
    List<OrderDetail> findOrders(String symbol, Set<OrderStatusEnum> status, int size);

    /**
     * 创建订单
     *
     * @param place 订单参数
     * @return 订单号
     */
    Optional<String> createOrder(Place place);

    /**
     * 撤消一个订单
     *
     * @param orderId
     * @return
     */
    Optional<String> submitcancel(String orderId);
}
