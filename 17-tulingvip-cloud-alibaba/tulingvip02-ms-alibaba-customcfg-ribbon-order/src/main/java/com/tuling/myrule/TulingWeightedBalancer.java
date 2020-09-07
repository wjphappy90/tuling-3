package com.tuling.myrule;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

import java.util.List;

/**
 * 根据权重选择随机选择一个
 * Created by smlz on 2019/11/21.
 */
public class TulingWeightedBalancer extends Balancer {

    public static Instance chooseInstanceByRandomWeight(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
