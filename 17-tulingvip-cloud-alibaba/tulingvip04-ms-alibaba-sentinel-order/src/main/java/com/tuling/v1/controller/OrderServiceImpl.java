package com.tuling.v1.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * Created by smlz on 2020/2/13.
 */
@Service
public class OrderServiceImpl {

    @SentinelResource("common")
    public String common() {
        return "common";
    }
}
