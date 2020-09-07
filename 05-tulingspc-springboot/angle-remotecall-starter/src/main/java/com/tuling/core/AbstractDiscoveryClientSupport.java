package com.tuling.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;

/**
 * Created by smlz on 2020/4/2.
 */
public abstract class AbstractDiscoveryClientSupport implements InitializingBean {

    private DiscoveryClient discoveryClient;

    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }

    public void afterPropertiesSet() throws Exception{

    }

}
