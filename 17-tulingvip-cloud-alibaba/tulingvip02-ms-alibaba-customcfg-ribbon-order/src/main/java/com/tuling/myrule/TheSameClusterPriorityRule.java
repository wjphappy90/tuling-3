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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 同一个集群优先调用策略
 * Created by smlz on 2019/11/21.
 */
@Slf4j
public class TheSameClusterPriorityRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    @Override
    public Server choose(Object key) {
        try {
            //第一步:获取当前服务所在的集群
            String currentClusterName = discoveryProperties.getClusterName();

            //第二步:获取一个负载均衡对象
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) getLoadBalancer();

            //第三步:获取当前调用的微服务的名称
            String invokedSerivceName = baseLoadBalancer.getName();

            //第四步:获取nacos clinet的服务注册发现组件的api
            NamingService namingService = discoveryProperties.namingServiceInstance();

            //第五步:获取所有的服务实例
            List<Instance> allInstance =  namingService.getAllInstances(invokedSerivceName);

            List<Instance> theSameClusterNameInstList = new ArrayList<>();

            //第六步:过滤筛选同集群下的所有实例
            for(Instance instance : allInstance) {
                if(StringUtils.endsWithIgnoreCase(instance.getClusterName(),currentClusterName)) {
                    theSameClusterNameInstList.add(instance);
                }
            }

            Instance toBeChooseInstance ;

            //第七步:选择合适的一个实例调用
            if(theSameClusterNameInstList.isEmpty()) {

                toBeChooseInstance = TulingWeightedBalancer.chooseInstanceByRandomWeight(allInstance);

                log.info("发生跨集群调用--->当前微服务所在集群:{},被调用微服务所在集群:{},Host:{},Port:{}",
                        currentClusterName,toBeChooseInstance.getClusterName(),toBeChooseInstance.getIp(),toBeChooseInstance.getPort());
            }else {
                toBeChooseInstance = TulingWeightedBalancer.chooseInstanceByRandomWeight(theSameClusterNameInstList);

                log.info("同集群调用--->当前微服务所在集群:{},被调用微服务所在集群:{},Host:{},Port:{}",
                        currentClusterName,toBeChooseInstance.getClusterName(),toBeChooseInstance.getIp(),toBeChooseInstance.getPort());
            }

            return new NacosServer(toBeChooseInstance);

        } catch (NacosException e) {
            log.error("同集群优先权重负载均衡算法选择异常:{}",e);
        }
        return null;
    }
}
