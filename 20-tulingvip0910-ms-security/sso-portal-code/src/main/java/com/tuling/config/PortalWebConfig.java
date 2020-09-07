package com.tuling.config;

import com.tuling.interceptor.CookieLoginInterceptor;
import com.tuling.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web安全配置
 * Created by smlz on 2019/12/29.
 */
@Configuration
public class PortalWebConfig implements WebMvcConfigurer {

/*    @Autowired
    private LoginInterceptor loginInterceptor;*/

    @Autowired
    private CookieLoginInterceptor cookieLoginInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home.html").setViewName("home");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieLoginInterceptor)
                .excludePathPatterns("/login","/logout","/home.html")
                .addPathPatterns("/order/createOrder/**","/product/**");

    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
