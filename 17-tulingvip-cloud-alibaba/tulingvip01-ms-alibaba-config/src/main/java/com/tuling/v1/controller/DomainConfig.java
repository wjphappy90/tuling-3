package com.tuling.v1.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by smlz on 2020/4/15.
 */
@ConfigurationProperties(prefix = "domain")
public class DomainConfig {

    private Map<String,Object> domainNumMap;

    public Map<String, Object> getDomainNumMap() {
        return domainNumMap;
    }

    public void setDomainNumMap(Map<String, Object> domainNumMap) {
        this.domainNumMap = domainNumMap;
    }
}
