package com.tuling.v1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 降级策略controller
 * Created by smlz on 2019/11/27.
 */
@RestController
@Slf4j
public class DeGradeDemoController {

    @RequestMapping("/testRt")
    public String testRTDeGrade() throws InterruptedException {
        Thread.sleep(500);
        log.info("rt 降级策略");
        return "rt";
    }

    @RequestMapping("/testExProp")
    public String testExceptionProportion() {
        log.info("异常比例");
        //numA[0-3]
        Integer numA = new Random().nextInt(4);
        //请求十次概率>1的概率是75%
        if(numA>1){
            throw new RuntimeException("throw  exception");
        }
        return "testExProp";
    }

    @RequestMapping("/testExCount")
    public String testExCount() {
        log.info("异常比例");
        //numA[0-3]
        Integer numA = new Random().nextInt(4);
        //请求十次概率>1的概率是75%
        if(numA>1){
            log.info("会抛出异常");
            throw new RuntimeException("throw  exception");
        }
        return "testExCount";
    }
}
