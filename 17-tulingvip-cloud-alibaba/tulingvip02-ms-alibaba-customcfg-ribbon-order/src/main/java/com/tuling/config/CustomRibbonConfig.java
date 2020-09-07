package com.tuling.config;

import com.ribbonconfig.GlobalRibbonConfig;
import com.ribbonconfig.PayCenterRibbonConfig;
import com.ribbonconfig.ProductCenterRibbonConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * Created by smlz on 2019/11/20.
 */
/*@Configuration
@RibbonClients(value = {
        @RibbonClient(name = "product-center",configuration = ProductCenterRibbonConfig.class),
        @RibbonClient(name = "pay-center",configuration = PayCenterRibbonConfig.class)
})*/
/**
 * ribbon的全局配置
 */
@RibbonClients(defaultConfiguration = GlobalRibbonConfig.class)

public class CustomRibbonConfig {

}
