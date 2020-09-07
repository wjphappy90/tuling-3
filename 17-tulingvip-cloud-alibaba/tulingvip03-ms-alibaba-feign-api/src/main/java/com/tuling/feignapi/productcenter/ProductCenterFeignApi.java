package com.tuling.feignapi.productcenter;

import com.tuling.config.ProductCenterFeignConfig;
import com.tuling.entity.ProductInfo;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by smlz on 2019/11/22.
 */

/**
 * 通过java配置版本来指定细粒度配置
 * */

/*
@FeignClient(name = "product-center",configuration = ProductCenterFeignConfig.class)
*/
//@FeignClient(name = "product-center")
public interface ProductCenterFeignApi {
    /**
     * 声明式接口,远程调用http://product-center/selectProductInfoById/{productNo}
     * @param productNo
     * @return
     */
    @RequestMapping("/selectProductInfoById/{productNo}")
    ProductInfo selectProductInfoById(@PathVariable("productNo") String productNo);

    /**
     * 修改锲约为Feign的  那么就可以使用默认的注解
     * @param
     * @return
     */

    @RequestMapping("/getToken4Header")
    String getToken4Header();
}
