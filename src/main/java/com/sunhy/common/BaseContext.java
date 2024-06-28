package com.sunhy.common;

import lombok.Getter;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 基于ThreadLocal的封装工具类
 * @Version 1.0
 */
public class BaseContext {
    //注意：不同的controller请求不是同一个线程
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    public static void setUserid(String userid) {
        BaseContext.userid = userid;
    }

    @Getter
    private static String userid;


    public static void setCurrenID(String id){
        THREAD_LOCAL.set(id);
    }

    public static String getCurrentID(){
        return THREAD_LOCAL.get();
    }
}
