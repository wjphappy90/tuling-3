package com.tuling.controller;

import com.tuling.entity.ProductInfo;
import com.tuling.remote.ProductRemoteCall;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by smlz on 2020/4/2.
 */
@RestController
public class TestController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private ProductRemoteCall productRemoteCall;

    @RequestMapping("/hehe")
    public ProductInfo test() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductName("华为p40");
        productInfo.setProductNo("1");
        productInfo.setProductPrice(5888);
        productInfo.setProductStore("99");
        return productRemoteCall.qryProductInfo3(productInfo);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
