package com.pengttyy.rest.client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.*;

@Component
public class CustomRestTemplate {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";
    public static final String TRADE_ROOT_URL = "api.huobi.pro";

    @Autowired
    private RestTemplate restTemplate;


    /**
     * https://api.huobi.pro/v1/order/orders?
     * AccessKeyId=e2xxxxxx-99xxxxxx-84xxxxxx-7xxxx&
     * order-id=1234567890&
     * SignatureMethod=HmacSHA256&
     * SignatureVersion=2&
     * Timestamp=2017-05-11T15%3A19%3A30&
     * Signature=4F65x5A2bLyMWVQj3Aqp%2BB4w%2BivaA7n5Oi2SuYtCJ9o%3D
     */
    public <T> ResponseEntity<T> tradeRestCall(String path, HttpEntity<?> entity, HttpMethod method,
                                               Class<T> responseType, Map<String, String> queryParam) {
        //添加所有参数
        Map<String, String> allParams = authenticationParams();
        allParams.putAll(queryParam);

        //计算签名
        String paramStr = paramToQueryStrAndEncode(allParams, true);
        String signatureValue = createSignature(TRADE_ROOT_URL, path, method, paramStr);

        logger.info("签名计算结果并进行Base64后的结果：{}", signatureValue);

        String url = "https://" + TRADE_ROOT_URL + path + "?" + paramStr + "&Signature=" + signatureValue;

        logger.info("发送到服务器的API请求：{}", url);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(url, method, null, responseType);
    }

    private String paramToQueryStrAndEncode(Map<String, String> params, boolean isEncode) {
        List<String> paramList = new ArrayList<>(params.size());
        params.forEach((String k, String v) -> {
            paramList.add(String.format("%s=%s", isEncode ? urlEncode(k) : k, isEncode ? urlEncode(v) : v));
        });
        paramList.sort(Comparator.naturalOrder());
        return StringUtils.collectionToCommaDelimitedString(paramList);
    }

    private String createSignature(String rootUrl, String path, HttpMethod method, String queryParamStr) {
        StringBuilder signatureTarget = new StringBuilder();
        signatureTarget.append(method.name()).append("\n")
                .append(rootUrl).append("\n")
                .append(path).append("\n")
                .append(queryParamStr);

        logger.info("签名计算的字符串：{}", signatureTarget.toString());
        return urlEncode(Base64.encodeBase64String(HmacUtils.hmacSha256(SECRET_KEY, signatureTarget.toString())));
    }

    /**
     * 返回认证参数
     *
     * @return
     */
    private Map<String, String> authenticationParams() {
        Map<String, String> params = new HashMap<>();
        params.put("AccessKeyId", ACCESS_KEY);
        params.put("SignatureMethod", "HmacSHA256");
        params.put("SignatureVersion", "2");
        String utc = DateFormatUtils.formatUTC(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
        params.put("Timestamp", utc);
        return params;
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            return value;
        }
    }


}
