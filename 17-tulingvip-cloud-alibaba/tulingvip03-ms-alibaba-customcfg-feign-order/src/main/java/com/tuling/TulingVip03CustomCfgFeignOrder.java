package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by smlz on 2019/11/22.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TulingVip03CustomCfgFeignOrder {

    public static void main(String[] args) {
        SpringApplication.run(TulingVip03CustomCfgFeignOrder.class, args);
    }
}
