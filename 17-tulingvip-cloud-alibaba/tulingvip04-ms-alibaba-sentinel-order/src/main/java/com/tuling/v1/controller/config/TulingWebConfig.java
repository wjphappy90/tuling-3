package com.tuling.v1.controller.config;

import com.tuling.v1.controller.interceptor.SentinelInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by smlz on 2020/2/19.
 */
@Configuration
public class TulingWebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SentinelInterceptors()).addPathPatterns("*");
    }
}
