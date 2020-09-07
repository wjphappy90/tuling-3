package com.tuling.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.Random;

/**
 * Created by smlz on 2020/4/2.
 */
@Slf4j
public class RandomChooseInstanceRule implements IChooseInstanceRules {
    @Override
    public ServiceInstance chooseInstance(List<ServiceInstance> serviceInstanceList) {
        if(serviceInstanceList!=null && !serviceInstanceList.isEmpty()) {

            Integer serverIndex  = new Random().nextInt(serviceInstanceList.size());

            return serviceInstanceList.get(serverIndex);
        }
        return null;
    }
}
