package com.tuling.rule;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * Created by smlz on 2020/4/2.
 */
public interface IChooseInstanceRules {

    ServiceInstance chooseInstance(List<ServiceInstance> serviceInstanceList);
}
