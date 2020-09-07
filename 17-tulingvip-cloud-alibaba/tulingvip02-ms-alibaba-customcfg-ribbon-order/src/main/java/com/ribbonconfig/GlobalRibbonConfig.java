package com.ribbonconfig;

import com.netflix.loadbalancer.IRule;
import com.tuling.myrule.TheSameClusterPriorityRule;
import com.tuling.myrule.TheSameClusterPriorityWithVersionRule;
import com.tuling.myrule.TulingWeightedRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by smlz on 2019/11/20.
 */
@Configuration
public class GlobalRibbonConfig {

    @Bean
    public IRule theSameClusterPriorityRule() {
        return new TulingWeightedRule();
        //return new TheSameClusterPriorityRule();
        //return new TheSameClusterPriorityWithVersionRule();
    }
}
