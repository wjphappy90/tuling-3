package com.tuling.utils;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by smlz on 2020/2/13.
 */
@Slf4j
public class BlockUtils {


    public static String testHelloSentinelV3BlockMethod(BlockException e){
        log.info("testHelloSentinelV3方法被流控了");
        return "testHelloSentinelV3方法被流控了";
    }
}
