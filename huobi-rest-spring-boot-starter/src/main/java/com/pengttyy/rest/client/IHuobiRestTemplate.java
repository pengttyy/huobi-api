package com.pengttyy.rest.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 20:27
 */
public interface IHuobiRestTemplate {
    /**
     * 发送get请求并返回泛型结果,仅用于data是一个对象的场景
     *
     * @param url          请求路径
     * @param responseType 泛型化的响应结果
     * @param uriVariables uri变量
     * @param <T>          操作结果
     * @return 统一泛型Bean
     * @throws RestClientException
     */
    <T> Result<T> getForBean(String url, ParameterizedTypeReference<Result<T>> responseType, Object... uriVariables) throws RestClientException;

    /**
     * 发送get请求并返回泛型List结果,仅用于data是一个集合的场景
     *
     * @param url          请求路径
     * @param responseType 泛型化的List响应结果
     * @param uriVariables uri变量
     * @param <T>          集合中的对象类型
     * @return 统一泛型List
     * @throws RestClientException
     */
    <T> Result<List<T>> getForList(String url, ParameterizedTypeReference<Result<List<T>>> responseType, Object... uriVariables) throws RestClientException;

    /**
     * 发送post请求并返回泛型结果,仅用于data是一个对象的场景
     *
     * @param url          请求路径
     * @param request      请求实体
     * @param responseType 响应的泛型类型
     * @param uriVariables uri变量
     * @param <T>          响应结果类型
     * @return 统一泛型Bean
     * @throws RestClientException
     */
    <T> Result<T> postForBean(String url, Object request, ParameterizedTypeReference<Result<T>> responseType, Object... uriVariables) throws RestClientException;

    /**
     * 发送post请求并返回泛型结果,仅用于data是一个list的场景
     *
     * @param url          请求路径
     * @param request      请求实体
     * @param responseType 响应的泛型类型
     * @param uriVariables uri变量
     * @param <T>          响应List中结果类型
     * @return 统一泛型List
     * @throws RestClientException
     */
    <T> Result<List<T>> postForList(String url, Object request, ParameterizedTypeReference<Result<List<T>>> responseType, Object... uriVariables) throws RestClientException;
}
