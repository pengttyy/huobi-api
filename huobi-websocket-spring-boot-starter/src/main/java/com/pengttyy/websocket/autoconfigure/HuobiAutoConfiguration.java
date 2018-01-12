package com.pengttyy.websocket.autoconfigure;

import com.pengttyy.websocket.entity.Kline;
import com.pengttyy.websocket.handler.DefaultHuobiWebSocketHandler;
import com.pengttyy.websocket.handler.message.DefaultHandleMessage;
import com.pengttyy.websocket.handler.message.IHandleMessage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
@Configuration
@AutoConfigureAfter(WebSocketAutoConfiguration.class)
@EnableScheduling
public class HuobiAutoConfiguration {
    private String webSocketUri = "wss://api.huobi.pro/ws";

    @Bean
    @ConditionalOnMissingBean
    public WebSocketConnectionManager wsConnectionManager(StandardWebSocketClient client, WebSocketHandler handler) {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client,
                handler, this.webSocketUri);
        manager.setAutoStartup(true);
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean
    public StandardWebSocketClient client() {
        return new StandardWebSocketClient();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebSocketHandler handler() {
        return new DefaultHuobiWebSocketHandler(handleMessage());
    }

    @Bean
    @ConditionalOnMissingBean
    public IHandleMessage handleMessage() {
        return new DefaultHandleMessage(Kline.class);
    }


}
