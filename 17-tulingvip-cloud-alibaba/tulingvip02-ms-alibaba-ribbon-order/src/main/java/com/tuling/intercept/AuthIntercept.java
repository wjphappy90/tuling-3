package com.tuling.intercept;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by smlz on 2020/2/7.
 */

@Slf4j
public class AuthIntercept implements HandlerInterceptor,InitializingBean {

    private static final String KEY_URL = "http://product-center/getKey" ;

    private String securetKey ;

    @Autowired
    private RestTemplate restTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        log.info("通过密钥校验token:{}",securetKey);
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        securetKey = restTemplate.postForObject(KEY_URL,null,String.class);
        log.info("获取的Key:{}",securetKey);
    }
}
