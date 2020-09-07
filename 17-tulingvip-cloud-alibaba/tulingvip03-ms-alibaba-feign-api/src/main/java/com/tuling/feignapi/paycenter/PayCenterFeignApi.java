package com.tuling.feignapi.paycenter;

import com.tuling.config.PayCenterFeignConfig;
import com.tuling.config.ProductCenterFeignConfig;
import com.tuling.entity.PayInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by smlz on 2020/2/22.
 */
@FeignClient(name = "pay-center",configuration = PayCenterFeignConfig.class)
public interface PayCenterFeignApi {

    @RequestMapping("/selectPayInfoByOrderNo/{orderNo}")
    PayInfo selectPayInfoByOrderNo(@PathVariable("orderNo") String orderNo);
}
