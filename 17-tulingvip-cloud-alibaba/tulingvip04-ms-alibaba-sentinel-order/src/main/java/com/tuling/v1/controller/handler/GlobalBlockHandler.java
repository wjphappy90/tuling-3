package com.tuling.v1.controller.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一处理限流类
 * Created by smlz on 2019/11/27.
 */
@Slf4j
public class GlobalBlockHandler {

    /**
     * 该方法用来处理限流的方法
     * @param testParam
     * @param ex
     * @return
     */
    public static String blockHandlerMethod(String testParam,BlockException ex) {
        log.info("被限流量了..."+ex.getMessage());
        return "invoke block......";
    }

}
