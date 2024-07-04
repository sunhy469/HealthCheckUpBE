package com.sunhy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 允许的前端域名
                .allowedMethods("*") // 允许的请求方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 允许携带认证信息（例如：cookie）
    }
}
