package com.pengttyy.rest;

import com.pengttyy.rest.interceptor.AuthenticationInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

/**
 * @Author pengttyy pengttyy@gmail.com
 */
@SpringBootApplication
public class HuobiRestApplication {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        DefaultUriTemplateHandler defaultUriTemplateHandler = new DefaultUriTemplateHandler();
        defaultUriTemplateHandler.setStrictEncoding(true);
        RestTemplateBuilder restTemplateBuilder = builder.additionalInterceptors(authenticationInterceptor())
                .uriTemplateHandler(defaultUriTemplateHandler)
                .setConnectTimeout(60 * 1000).additionalCustomizers();
        return restTemplateBuilder.build();
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }



    public static void main(String[] args) {
        SpringApplication.run(HuobiRestApplication.class, args);
    }
}
