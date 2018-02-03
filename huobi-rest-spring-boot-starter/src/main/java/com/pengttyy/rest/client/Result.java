package com.pengttyy.rest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 20:04
 */
@Data
public class Result<T> {
    private String status;
    private T data;
    private Date ts;
    @JsonProperty("err-code")
    private String errCode;
    @JsonProperty("err-msg")
    private String errMsg;

    public Optional<T> getData() {
        return Optional.ofNullable(this.data);
    }


    public T getDataThrows() {
        return Optional.ofNullable(this.data).orElseThrow(() ->
                new NullPointerException(this.getErrCode() + "\t" + this.getErrMsg()));
    }

    /**
     * 请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return "ok".equals(this.status);
    }

}
