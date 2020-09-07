package com.tuling.seata.service;


import com.tuling.seata.domin.Order;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:order服务接口
* @author: smlz
* @createDate: 2019/12/9 20:31
* @version: 1.0
*/
public interface IOrderService {

    void createOrder(Order order);
}
