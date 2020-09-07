package com.tuling.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.ahas.sentinel.web.SentinelWebInterceptor;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.tuling.handler.GlobalExceptionHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by smlz on 2019/11/18.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(
            blockHandler = "handleException",blockHandlerClass = GlobalExceptionHandler.class,
            fallback = "fallback",fallbackClass = GlobalExceptionHandler.class

    )
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
