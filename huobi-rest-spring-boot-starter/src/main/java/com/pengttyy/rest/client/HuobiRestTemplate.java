package com.pengttyy.rest.client;

import com.pengttyy.rest.entity.Result;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * huobi请求的RestTemplate
 *
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 14:45
 */
public class HuobiRestTemplate extends RestTemplate implements IHuobiRestTemplate {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";

    @Override
    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUri(url);
        UriComponents uriComponents = uriComponentsBuilder.build();

        MultiValueMap<String, String> allQueryParams = appendAuthenticationParams(uriComponents.getQueryParams());

        //todo java8收集器
        //参数排序
        Set<String> keys = allQueryParams.keySet();
        MultiValueMap<String, String> newQueryParams = new LinkedMultiValueMap<>();
        keys.stream().sorted().forEach(key -> newQueryParams.put(key, allQueryParams.get(key)));

        //拼接查询参数
        StringBuilder queryParamsStr = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : newQueryParams.entrySet()) {
            String key = entry.getKey();
            queryParamsStr.append(String.format("%s=%s", key, newQueryParams.getFirst(key))).append("&");
        }
        queryParamsStr = queryParamsStr.deleteCharAt(queryParamsStr.length() - 1);
        logger.debug("计算签名的字符串：{}", queryParamsStr.toString());

        //计算签名
        String signature = createSignature(uriComponents.getHost(), uriComponents.getPath(), method, queryParamsStr.toString());
        logger.debug("签名值的字符串：{}", signature);
        uriComponentsBuilder.replaceQueryParams(newQueryParams).queryParam("Signature", signature);
        UriComponents build = uriComponentsBuilder.build(true);

        URI newURI = build.toUri();
        logger.debug("最终请求的URI：{}", newURI);
        return super.doExecute(newURI, method, requestCallback, responseExtractor);
    }

    /**
     * 添加认证参数
     *
     * @return
     */
    private MultiValueMap<String, String> appendAuthenticationParams(MultiValueMap<String, String> queryParams) {
        LinkedMultiValueMap<String, String> allParams = new LinkedMultiValueMap<>();
        allParams.putAll(queryParams);
        allParams.add("AccessKeyId", ACCESS_KEY);
        allParams.add("SignatureMethod", "HmacSHA256");
        allParams.add("SignatureVersion", "2");
        String utc = DateFormatUtils.formatUTC(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
        allParams.add("Timestamp", urlEncode(utc));

        return allParams;
    }

    /**
     * 对请求签名
     *
     * @param rootUrl       host
     * @param path          路径
     * @param method        请求方法
     * @param queryParamStr 查询参数
     * @return 签名值
     */
    private String createSignature(String rootUrl, String path, HttpMethod method, String queryParamStr) {
        StringBuilder signatureTarget = new StringBuilder();
        signatureTarget.append(method.name()).append("\n")
                .append(rootUrl).append("\n")
                .append(path).append("\n")
                .append(queryParamStr);

        logger.debug("签名计算的字符串：{}", signatureTarget.toString());
        return urlEncode(Base64.encodeBase64String(HmacUtils.hmacSha256(SECRET_KEY, signatureTarget.toString())));
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            return value;
        }
    }

    @Override
    public <T> Result<T> getForBean(String url, ParameterizedTypeReference<Result<T>> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<Result<T>> exchange = this.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        return exchange.getBody();
    }

    @Override
    public <T> Result<List<T>> getForList(String url, ParameterizedTypeReference<Result<List<T>>> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<Result<List<T>>> exchange = this.exchange(url, HttpMethod.GET, null, responseType, uriVariables);
        return exchange.getBody();
    }

    @Override
    public <T> Result<T> postForBean(String url, Object request, ParameterizedTypeReference<Result<T>> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<Result<T>> exchange = this.exchange(url, HttpMethod.POST, request == null ? null : new HttpEntity<>(request), responseType, uriVariables);
        return exchange.getBody();
    }

    @Override
    public <T> Result<List<T>> postForList(String url, Object request, ParameterizedTypeReference<Result<List<T>>> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<Result<List<T>>> exchange = this.exchange(url, HttpMethod.POST, request == null ? null : new HttpEntity<>(request), responseType, uriVariables);
        return exchange.getBody();
    }
}
