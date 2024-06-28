package com.sunhy.interceptor;

import com.sunhy.utils.JavaWebToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("token拦截器");
        // 从请求头中获取 token
        String token = request.getHeader("Authorization");


        // 去掉 "Bearer " 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 token
        if (isValidToken(token)) {
            return true; // 放行请求
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置未授权状态码
            return false; // 拦截请求
        }
    }

    private boolean isValidToken(String token) {
        // 在这里进行 token 验证逻辑，可以使用自己的方法或者工具类
        Map<String, Object> map = JavaWebToken.parserJavaWebToken(token);
        // 返回 true 表示 token 有效，返回 false 表示 token 无效
        return map != null;
    }
}

