package com.pengttyy.rest.autoconfigure;

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.client.IHuobiRestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/7 12:50
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties(HuobiRestProperties.class)
public class HuobiRestAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public IHuobiRestTemplate restTemplate(RestTemplateBuilder builder, HuobiRestProperties properties) {
        return builder.setConnectTimeout(60 * 1000).rootUri(properties.getRootUrl()).build(HuobiRestTemplate.class);
    }
}
