package com.xingyun.mysteryjob.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @fileName: WebAppConfig
 * @createDate: 2019-07-03 14:13.
 * @description: web配置
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {


    /**
     * 添加 CORS 跨域处理
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(false)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }


}