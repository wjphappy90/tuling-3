package com.tuling.v1.controller;

import com.tuling.entity.ProductInfo;
import com.tuling.mapper.ProductInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
public class ProductInfoController{


    @Autowired
    private ProductInfoMapper productInfoMapper;

    @RequestMapping("/selectProductInfoById/{productNo}")
    public ProductInfo selectProductInfoById(@PathVariable("productNo") String productNo) throws InterruptedException {
        log.info("我被调用了");
        Thread.sleep(500);
        ProductInfo productInfo = productInfoMapper.selectProductInfoById(productNo);
        return productInfo;
    }

    @RequestMapping("/getToken4Header")
    public String getToken4Header(@RequestHeader("token") String token)  {
        log.info("token:{}",token);
        return token;
    }
}
