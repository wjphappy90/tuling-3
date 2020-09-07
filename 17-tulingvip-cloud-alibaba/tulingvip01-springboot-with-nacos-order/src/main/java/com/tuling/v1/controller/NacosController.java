package com.tuling.v1.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by smlz on 2020/2/3.
 */
@RestController
@RequestMapping("/nacos")
public class NacosController {

    @NacosInjected
    private NamingService namingService;

    //测试地址: http://localhost:8080/nacos/get?serviceName=order-center
    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public List<Instance> get(@RequestParam String serviceName) throws NacosException {

        return namingService.getAllInstances(serviceName);
    }

    //测试地址: http://localhost:8080/nacos/register
    @RequestMapping("/register")
    public String register() throws NacosException {
        namingService.registerInstance("order-center","127.0.0.1",8081);
        return "ok";
    }

}
