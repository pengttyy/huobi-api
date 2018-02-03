package com.pengttyy.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/2/2 23:47
 */
public class WebSocketClient {
    private static final String WSS = "wss";
    private int port;
    private URI uri;
    private EventLoopGroup group;
    private SslContext sslCtx;
    private Channel channel;

    public WebSocketClient(String url, int thread) throws SSLException, URISyntaxException {
        this.uri = new URI(url);
        init();
        this.group = new NioEventLoopGroup(thread);
    }

    private void init() throws SSLException {
        boolean isWSS = uri.getScheme().equals(WSS);
        if (isWSS) {
            this.sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }
        setPort(isWSS);
    }

    private void setPort(boolean isWSS) {
        if (this.uri.getPort() == -1) {
            if (isWSS) {
                this.port = 443;
            } else {
                this.port = 80;
            }
        } else {
            this.port = this.uri.getPort();
        }
    }

    private int getPort() {
        return this.port;
    }

    public void connect() throws Exception {
        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (sslCtx != null) {
                            pipeline.addLast(sslCtx.newHandler(ch.alloc(), uri.getHost(), getPort()));
                        }
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024));
                        pipeline.addLast(new WebSocketClientProtocolHandler(webSocketClientHandshaker, false));
                        pipeline.addLast(new MyWebSocketFrameHandler());
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), this.getPort()).sync();
        this.channel = channelFuture.channel();
        channel.closeFuture().addListener((future) -> {
            if (future.isSuccess()) {
                final EventLoop loop = channel.eventLoop();
                loop.schedule(() -> {
                    try {
                        System.out.println("重连！！");
                        connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.close();
                    }
                }, 2, TimeUnit.SECONDS);
            }
        });
    }

    public void close() {
        group.shutdownGracefully().addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("客户端关闭！");
            }
        });
    }

    public static void main(String[] args) throws Exception {
        System.out.println("test");
        WebSocketClient webSocketClient = new WebSocketClient("wss://api.huobi.pro/ws", 2);
        webSocketClient.connect();
        //myWebSocketClient.subscribe("{\"sub\":\"market.xrpusdt.kline.1min\",\"id\": \"id1\"}");
    }

}
