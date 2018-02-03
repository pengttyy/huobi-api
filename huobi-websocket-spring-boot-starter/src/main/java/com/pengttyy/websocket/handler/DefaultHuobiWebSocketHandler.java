package com.pengttyy.websocket.handler;


import com.pengttyy.websocket.handler.message.IHandleMessage;
import com.pengttyy.websocket.handler.util.GzipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.nio.ByteBuffer;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
public class DefaultHuobiWebSocketHandler extends AbstractWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String PING = "ping";
    private static final String PONG = "pong";
    private static final String STATUS = "status";
    private IHandleMessage handleMessage;
    private WebSocketSession session;

    @Autowired
    private ApplicationContext applicationContext;

    public DefaultHuobiWebSocketHandler(IHandleMessage handleMessage) {
        this.handleMessage = handleMessage;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        session.sendMessage(new TextMessage(handleMessage.subscribe()));
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer payload = message.getPayload();
        String serverMsg = new String(GzipUtil.decompress(payload.array()));
        logger.debug("context:[{}]", serverMsg);

        if (serverMsg.contains(PING)) {
            String clentMsg = serverMsg.replaceAll(PING, PONG);
            logger.debug("heart:[{}]", clentMsg);
            session.sendMessage(new TextMessage(clentMsg));
        } else if (serverMsg.contains(PONG)) {
            logger.debug("接收服务器返回的心跳：{}", serverMsg);
        } else if (serverMsg.contains(STATUS)) {
            handleMessage.subscribeCallback(serverMsg);
        } else {
            handleMessage.call(serverMsg);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.debug("handleTextMessage:[{}]", message.getPayload());
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        logger.debug("handlePongMessage:[{}]", message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        handleMessage.webSocketError(exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.warn("失败后自动重连!!");
        try {
            WebSocketConnectionManager bean = this.applicationContext.getBean(WebSocketConnectionManager.class);
            bean.stop();
            bean.start();
            logger.warn("失败后重连成功!!");
        } catch (Exception e) {
            logger.error("重启websocket异常", e);
        }
        handleMessage.afterConnectionClosed();
    }
}
