package com.tuling.handler;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.tuling.entity.ProductInfo;
import com.tuling.feignapi.sentinel.ProductCenterFeignApiWithSentinel;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2019/11/28.
 */
@Component
@Slf4j
public class ProductCenterFeignApiWithSentielFallbackFactoryasdasf implements FallbackFactory<ProductCenterFeignApiWithSentinel> {
    @Override
    public ProductCenterFeignApiWithSentinel create(Throwable throwable) {
        return new ProductCenterFeignApiWithSentinel(){

            @Override
            public ProductInfo selectProductInfoById(String productNo) {
                ProductInfo productInfo = new ProductInfo();
                if (throwable instanceof FlowException) {
                    log.error("流控了....{}",throwable.getMessage());
                    productInfo.setProductName("我是被流控的默认商品");
                }else {
                    log.error("降级了....{}",throwable.getMessage());
                    productInfo.setProductName("我是被降级的默认商品");
                }

                return productInfo;
            }
        };
    }
}
