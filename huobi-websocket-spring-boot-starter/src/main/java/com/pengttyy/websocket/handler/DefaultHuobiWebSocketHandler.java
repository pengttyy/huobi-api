package com.pengttyy.websocket.handler;


import com.pengttyy.websocket.handler.message.IHandleMessage;
import com.pengttyy.websocket.handler.util.GzipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

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
    private Random random = new Random();

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
        if (serverMsg.contains(PING)) {
            String clentMsg = serverMsg.replaceAll(PING, PONG);
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
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        handleMessage.webSocketError(exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        handleMessage.afterConnectionClosed();
    }

    /**
     * 定时发送心跳
     *
     * @throws IOException
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void sendPingMessage() throws IOException {
        if (this.session != null) {
            String payload = "{\"ping\": " + random.nextLong() + "}";
            this.session.sendMessage(new TextMessage(payload));
            logger.debug("发送心跳信息：{}", payload);
        }
    }

}
