package com.tuling.v1.controller;

import com.tuling.entity.ProductInfo;
import com.tuling.mapper.ProductInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@RequestMapping("/product")
public class ProductInfoController {


    @Autowired
    private ProductInfoMapper productInfoMapper;

    @RequestMapping("/selectProductInfoById/{productNo}")
    public Object selectProductInfoById(@PathVariable("productNo") String productNo) {

        ProductInfo productInfo = productInfoMapper.selectProductInfoById(productNo);
        return productInfo;
    }

    @RequestMapping("/getProduct/{productId}")
    ProductInfo qryProductInfo(@PathVariable String productId) {
        ProductInfo productInfo = productInfoMapper.selectProductInfoById(productId);
        return productInfo;
    }

    @RequestMapping(value = "/qryProductInfo",method = {RequestMethod.GET})
    ProductInfo qryProductInfo2(@RequestParam("productId") String productId){
        ProductInfo productInfo = productInfoMapper.selectProductInfoById(productId);
        return productInfo;
    }

    @RequestMapping(value = "/qryProductInfo2",method = {RequestMethod.POST})
    ProductInfo qryProductInfo3(@RequestBody ProductInfo productInfo){
        return productInfo;
    }
}
