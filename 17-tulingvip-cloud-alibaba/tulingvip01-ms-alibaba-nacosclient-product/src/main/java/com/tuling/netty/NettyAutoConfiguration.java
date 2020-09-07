package com.tuling.netty;

import com.alibaba.cloud.nacos.NacosDiscoveryAutoConfiguration;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@EnableConfigurationProperties(NettyServerProperties.class)
@AutoConfigureAfter(NacosDiscoveryAutoConfiguration.class)
public class NettyAutoConfiguration {

    @Bean
    public NettyServer nettyServer(){
        return new NettyServer();
    }
}
