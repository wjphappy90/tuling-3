package com.tuling.cfg;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * Created by smlz on 2019/7/15.
 */
@Configuration
@AutoConfigureBefore(value = {CfgB.class})
public class CfgA {

    public CfgA() {
        System.out.println("加载A");
    }
}
