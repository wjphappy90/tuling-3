package com.tuling.config;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by smlz on 2019/11/18.
 */
@Configuration
public class WebConfig {

    @Bean
    public RestTemplate restTemplate(DiscoveryClient discoveryClient) {
        return new TulingRestTemplate(discoveryClient);
    }
}
