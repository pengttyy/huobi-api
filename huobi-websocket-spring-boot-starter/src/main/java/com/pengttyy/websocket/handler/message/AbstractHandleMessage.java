package com.pengttyy.websocket.handler.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengttyy.websocket.entity.SubscribeFailure;
import com.pengttyy.websocket.entity.SubscribeSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 订阅数据处理类，对服务器返回的数据进行处理
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
public abstract class AbstractHandleMessage<T> implements IHandleMessage {
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ObjectMapper objectMapper;

    private Class<T> clazz;

    public AbstractHandleMessage(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 需要订阅的目标数据
     *
     * @return
     */
    @Override
    public abstract String subscribe();

    @Override
    public void subscribeCallback(String serverMsg) {
        try {
            JsonNode jsonNode = this.objectMapper.readTree(serverMsg);
            if (jsonNode.hasNonNull(STATUS)) {
                JsonNode status = jsonNode.get(STATUS);
                if (ERROR.equals(status)) {
                    subscribeFailed(this.objectMapper.convertValue(jsonNode, SubscribeFailure.class));
                } else {
                    subscribeSucceed(this.objectMapper.convertValue(jsonNode, SubscribeSuccess.class));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅成功时websocket返回消息处理
     *
     * @param subscribeSuccess
     */
    protected void subscribeSucceed(SubscribeSuccess subscribeSuccess) {
        logger.info("订阅成功:{}", subscribeSuccess);
    }

    /**
     * 订阅失败时websocket返回消息处理
     *
     * @param error
     */
    protected void subscribeFailed(SubscribeFailure error) {
        logger.info("订阅失败:{}", error);
    }

    @Override
    public void call(String message) {
        JsonNode jsonNode = null;
        try {
            jsonNode = this.objectMapper.readTree(message);
            JsonNode tick = jsonNode.get("tick");
            success(this.objectMapper.convertValue(tick, this.clazz));
        } catch (Exception e) {
            failed(e);
        }
    }

    /**
     * 接收websocket服务成功消息推送
     *
     * @param tick
     */
    protected abstract void success(T tick);

    /**
     * 处理服务端消息时异常
     *
     * @param e
     */
    protected void failed(Exception e) {
        logger.error("处理消息时异常", e);
    }

    @Override
    public void webSocketError(Throwable exception) {
        logger.error("websocket异常", exception);
    }

    @Override
    public void afterConnectionClosed() {
        logger.error("连接关闭通知！！");
    }
}
