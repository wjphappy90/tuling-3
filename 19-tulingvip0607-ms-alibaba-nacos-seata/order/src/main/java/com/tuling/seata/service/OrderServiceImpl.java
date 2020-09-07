package com.tuling.seata.service;

import com.tuling.seata.api.RemoteAccountService;
import com.tuling.seata.api.RemoteStorageService;
import com.tuling.seata.domin.Order;
import com.tuling.seata.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author lihaodong
 * @since 2019-11-25
 */
@Slf4j
@Service
public class OrderServiceImpl  implements IOrderService {

    @Autowired
    private RemoteStorageService remoteStorageService;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public void createOrder(Order order) {
        log.info("下单开始,用户:{},商品:{},数量:{},金额:{}", order.getUserId(), order.getProductId(), order.getCount(), order.getPayMoney());
        //创建订单
        order.setStatus(0);
        orderMapper.saveOrder(order);
        log.info("保存订单{}", order);

        //远程调用库存服务扣减库存
        log.info("扣减库存开始");

        remoteStorageService.reduceCount(order.getProductId(), order.getCount());

        log.info("扣减库存结束");


        System.out.println(1/0);
        //修改订单状态为已完成
        log.info("修改订单状态开始");
        orderMapper.updateOrderStatusById(order.getId(),1);
        log.info("修改订单状态结束");

        log.info("下单结束");
    }
}
