package com.pengttyy.websocket.handler.message;

import com.pengttyy.websocket.entity.Kline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认消息处理，自定义处理时扩展AbstractHandleMessage类
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2017/12/28 15:21
 */
public class DefaultHandleMessage extends AbstractHandleMessage<Kline> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultHandleMessage(Class<Kline> clazz) {
        super(clazz);
    }

    @Override
    public String subscribe() {
        return "{\"sub\":\"market.xrpusdt.kline.5min\",\"id\": \"id1\"}";
    }

    @Override
    protected void success(Kline tick) {
        logger.info("{}", tick);
    }
}
