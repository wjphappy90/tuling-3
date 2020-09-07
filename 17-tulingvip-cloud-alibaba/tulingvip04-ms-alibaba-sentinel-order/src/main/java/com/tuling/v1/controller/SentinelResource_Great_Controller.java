package com.tuling.v1.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.tuling.v1.controller.handler.GlobalBlockHandler;
import com.tuling.v1.controller.handler.GlobalFallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试SentinelResource的注解
 * Created by smlz on 2019/11/27.
 */
@RestController
@Slf4j
public class SentinelResource_Great_Controller {


    @SentinelResource(value = "testSentinelResourceGreat",
            blockHandlerClass = GlobalBlockHandler.class,
            fallbackClass = GlobalFallbackHandler.class,
            blockHandler = "blockHandlerMethod",
            fallback = "fallBackHandlerMethod"
    )
    @RequestMapping("/testSentinelApi-great")
    public String testSentinelResource(@RequestParam(value = "testParam",required = false) String testParam) throws InterruptedException {

        Thread.sleep(200);
        return "you param is:"+testParam;
    }
}
