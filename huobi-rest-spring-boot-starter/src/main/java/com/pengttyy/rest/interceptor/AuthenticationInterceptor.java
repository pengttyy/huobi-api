package com.pengttyy.rest.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        logger.info("URI:{}", request.getURI().toURL().toString());
        HttpHeaders headers = request.getHeaders();
        System.out.println(headers.toString());
        return clientHttpRequestExecution.execute(request, body);
    }
}
