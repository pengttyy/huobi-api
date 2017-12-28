package com.pengttyy.websocket.handler.message;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
public interface IHandleMessage {
    /**
     * 需要订阅的目标
     *
     * @return
     */
    String subscribe();

    /**
     * 订阅成功消息处理
     *
     * @param serverMessage
     */
    void subscribeCallback(String serverMessage);

    /**
     * 服务器返回数据回调处理
     *
     * @param message
     */
    void call(String message);

    /**
     * webSocket连接异常处理
     *
     * @param exception
     */
    void webSocketError(Throwable exception);

    /**
     * connection连接关闭通知
     */
    void afterConnectionClosed();

}
