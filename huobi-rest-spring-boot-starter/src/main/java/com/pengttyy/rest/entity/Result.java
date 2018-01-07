package com.pengttyy.rest.entity;

import java.util.Optional;

/**
 * @author pengttyy pengttyy@gmail.com
 * @date 2018/1/6 20:04
 */
public class Result<T> {
    private String status;
    private T data;

    public void setStatus(String status) {
        this.status = status;
    }

    public Optional<T> getData() {
        return Optional.ofNullable(this.data);
    }

    public void setData(T data) {
        this.data = data;
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
