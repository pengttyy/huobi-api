package com.pengttyy.netty;

import com.pengttyy.netty.util.GzipUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler.ClientHandshakeStateEvent;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/2/3 0:19
 */
public class MyWebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final String PING = "ping";
    private static final String PONG = "pong";
    private static final String STATUS = "status";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binary = (BinaryWebSocketFrame) frame;
            ByteBuf content = binary.content();
            byte[] bytes = new byte[content.readableBytes()];
            content.readBytes(bytes);
            String serverMsg = GzipUtil.decompress(bytes);


            if (serverMsg.contains(PING)) {
                String clentMsg = serverMsg.replaceAll(PING, PONG);
                ctx.writeAndFlush(new TextWebSocketFrame(clentMsg));
            } else if (serverMsg.contains(STATUS)) {
                System.out.println("订阅消息" + serverMsg);
            } else {
                System.out.println(serverMsg);
            }
        } else {
            System.out.println(frame);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof ClientHandshakeStateEvent) {
            switch ((ClientHandshakeStateEvent) evt) {
                case HANDSHAKE_COMPLETE:
                    ctx.writeAndFlush(new TextWebSocketFrame("{\"sub\":\"market.xrpusdt.kline.1min\",\"id\": \"id1\"}"));
                    break;
                case HANDSHAKE_ISSUED:
                    break;
                default:
                    System.out.println("启动握手，服务器还未响应");
            }

        }
    }
}
