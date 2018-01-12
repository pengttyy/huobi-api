package com.pengttyy.rest.autoconfigure;

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.interceptor.LogInterceptor;
import com.pengttyy.rest.service.IAccountService;
import com.pengttyy.rest.service.IorderService;
import com.pengttyy.rest.service.impl.AccountServiceImpl;
import com.pengttyy.rest.service.impl.OrderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

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
    public HuobiRestTemplate restTemplate(RestTemplateBuilder builder, HuobiRestProperties properties, Collection<? extends ClientHttpRequestInterceptor> interceptors) {
        return builder.setConnectTimeout(1000).setReadTimeout(2000)
                .rootUri(properties.getRootUrl())
                .additionalInterceptors(interceptors)
                .build(HuobiRestTemplate.class);
    }

    @Bean
    public ClientHttpRequestInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public IorderService orderService() {
        return new OrderServiceImpl();
    }

    @Bean
    public IAccountService accountService() {
        return new AccountServiceImpl();
    }

}
