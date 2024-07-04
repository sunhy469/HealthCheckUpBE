package com.sunhy.config;

import com.sunhy.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    public InterceptorConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 添加拦截器并指定拦截的路径
//        registry.addInterceptor(tokenInterceptor)
////                .addPathPatterns("")//需要拦截的路径
//                .excludePathPatterns("/signin", "/","/login")
//                .order(0);
//    }
}

