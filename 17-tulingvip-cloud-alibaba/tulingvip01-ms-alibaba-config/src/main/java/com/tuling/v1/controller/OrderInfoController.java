package com.tuling.v1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
@RefreshScope
@EnableConfigurationProperties(DomainConfig.class)
public class OrderInfoController {

    @Autowired
    private DomainConfig domainConfig;

    //@Value("${isNewBusi}")
    private Boolean isNewBusi;

    @Autowired
    private DiscoveryClient discoveryClient;


    @RequestMapping("/selectOrderInfoById/{orderNo}")
    public Object selectOrderInfoById(@PathVariable("orderNo") String orderNo) {
        System.out.println(domainConfig.getDomainNumMap());
        log.info("是否业务走新逻辑:{}",isNewBusi);
        if(isNewBusi) {
            return "查询订单执行新逻辑->execute new busi : "+orderNo;
        }
        return "查询订单执行老逻辑->execute old busi : "+orderNo;

    }

    @GetMapping("/getServiceList")
    public List<ServiceInstance> getServiceList() {
        List<ServiceInstance> serviceInstanceList =  discoveryClient.getInstances("order-center");
        return serviceInstanceList;
    }
}
