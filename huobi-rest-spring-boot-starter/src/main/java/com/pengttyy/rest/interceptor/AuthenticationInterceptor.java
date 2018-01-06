package com.pengttyy.rest.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 14:45
 */
public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        logger.info("URI:{}", request.getURI().toURL().toString());
        return clientHttpRequestExecution.execute(request, body);
    }
}
