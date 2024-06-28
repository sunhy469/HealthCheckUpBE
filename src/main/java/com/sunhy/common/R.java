package com.sunhy.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String message; //错误信息

    private T data; //数据

    private String token; //token

    private String id;

    private String avatar;


    public static <T> R<T> success(T object,String token,String id,String avatar) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        r.token= token;
        r.id=id;
        r.avatar=avatar;
        return r;
    }
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> success(String message) {
        R<T> r = new R<T>();
        r.message = message;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String message) {
        R r = new R();
        r.message = message;
        r.code = 0;
        return r;
    }

}
