package com.tuling.v1.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试SentinelResource的注解(可以优化)
 * Created by smlz on 2019/11/27.
 */
@RestController
@Slf4j
public class SentinelResourceController {

    /**
     * 官网解释： https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
     * 特别地，若 blockHandler 和 fallback 都进行了配置，
     * 则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑。
     * 若未配置 blockHandler、fallback 和 defaultFallback，
     * 则被限流降级时会将 BlockException 直接抛出（
     * 若方法本身未定义 throws BlockException 则会被 JVM 包装一层 UndeclaredThrowableException）。
     * @param testParam
     * @return
     */
    @SentinelResource(value = "testSentinelResource",
            /*blockHandler = "block",*/fallback ="fallback")
    @RequestMapping("/testSentinelApi")
    public String testSentinelResource(@RequestParam(value = "testParem",required = false) String testParam) {

        if(testParam == null) {
            throw new IllegalArgumentException("param can not be null");
        }

        return "you param is:"+testParam;
    }

    /**
     * 该方法用来处理限流的方法
     * @param testParam
     * @param ex
     * @return
     */
    public String block(String testParam,BlockException ex) {
        log.info("被限流量了..."+ex.getMessage());
        return "invoke block......";
    }

    /**
     * 用来处理容错降级的方法
     * @param testParam
     * @param ex
     * @return
     */
    public String fallback(String testParam,Throwable ex) {
        log.info("被降级拉......"+ex.getMessage());
        return "invoke fallback......";
    }


}
