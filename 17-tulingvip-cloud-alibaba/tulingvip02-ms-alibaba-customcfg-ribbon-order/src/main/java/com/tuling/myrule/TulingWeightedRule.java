package com.tuling.myrule;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by smlz on 2019/11/20.
 */
@Slf4j
public class TulingWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        //读取配置文件并且初始化,ribbon内部的 几乎用不上
    }


    @Override
    public Server choose(Object key) {
        try {
            log.info("key:{}",key);
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            log.info("baseLoadBalancer--->:{}",baseLoadBalancer);

            //获取微服务的名称
            String serviceName = baseLoadBalancer.getName();

            //获取Nocas服务发现的相关组件API
            NamingService namingService =  discoveryProperties.namingServiceInstance();

            //获取 一个基于nacos client 实现权重的负载均衡算法
            Instance instance = namingService.selectOneHealthyInstance(serviceName);

            //返回一个server
            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("自定义负载均衡算法错误");
        }
        return null;
    }
}
