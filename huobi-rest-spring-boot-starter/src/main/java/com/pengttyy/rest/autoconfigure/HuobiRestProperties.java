package com.pengttyy.rest.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 12:52
 */
@ConfigurationProperties(prefix = "huobi.rest")
@Data
public class HuobiRestProperties {
    private String rootUrl = "https://api.huobi.pro";
    private String accessKey;
    private String secretKey;
}
