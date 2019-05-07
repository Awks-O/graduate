package cn.core.utils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ResultCode code;
    private final T data;
    private String message;

    private Result(ResultCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <E> Result<E> failure(ResultCode code) {
        return new Result<>(code, null, null);
    }

    public static <E> Result<E> failure(ResultCode code, String msg) {
        return new Result<>(code, msg, null);
    }

    public static <E> Result<E> success() {
        return new Result<>(ResultCode.SUCCESS, null, null);
    }

    public static <E> Result<E> success(E data) {
        return new Result<>(ResultCode.SUCCESS, null, data);
    }

    public static <E> Result<E> success(E data, String message) {
        return new Result<>(ResultCode.SUCCESS, message, data);
    }


    public ResultCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}