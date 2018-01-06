package com.pengttyy.rest;

import com.pengttyy.rest.client.HuobiRestTemplate;
import com.pengttyy.rest.client.IHuobiRestTemplate;
import com.pengttyy.rest.interceptor.AuthenticationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @Author pengttyy pengttyy@gmail.com
 */
@SpringBootApplication
public class HuobiRestApplication {

    @Bean
    public IHuobiRestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplateBuilder restTemplateBuilder = builder.additionalInterceptors(authenticationInterceptor())
                .setConnectTimeout(60 * 1000).rootUri("https://api.huobi.pro");
        return restTemplateBuilder.build(HuobiRestTemplate.class);
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    public static void main(String[] args) {
        SpringApplication.run(HuobiRestApplication.class, args);
    }
}
