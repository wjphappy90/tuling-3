package com.tuling.v1.controller;

import com.tuling.entity.ProductInfo;
import com.tuling.mapper.ProductInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
public class ProductInfoController {

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @GetMapping("/selectProductInfoById/{productNo}")
    public Object selectProductInfoById(@PathVariable("productNo") String productNo) throws InterruptedException {

        log.info("我被请求了:{}",port);
        //Thread.sleep(5000);
        ProductInfo productInfo = productInfoMapper.selectProductInfoById(productNo);
        return productInfo;
    }

    @PostMapping("/getKey")
    public String getSecretKey() throws InterruptedException {
        log.info("我被请求了:{}",port);
        Thread.sleep(3000);
        return "tulingKey";
    }
}
