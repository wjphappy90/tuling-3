package com.tuling.v1.controller;

import com.tuling.entity.OrderInfo;
import com.tuling.entity.ProductInfo;
import com.tuling.feignapi.productcenter.ProductCenterFeignApi;
import com.tuling.mapper.OrderInfoMapper;
import com.tuling.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
public class OrderInfoController {

    @Autowired
    private ProductCenterFeignApi productCenterFeignApi;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderServiceImpl orderServiceImpl;

    @RequestMapping("/selectOrderInfoById/{orderNo}")
    public Object selectOrderInfoById(@PathVariable("orderNo") String orderNo) {

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderNo);
        if(null == orderInfo) {
            return "根据orderNo:"+orderNo+"查询没有该订单";
        }

        ProductInfo productInfo = productCenterFeignApi.selectProductInfoById(orderNo);

        if(productInfo == null) {
            return "没有对应的商品";
        }

        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(orderInfo.getOrderNo());
        orderVo.setUserName(orderInfo.getUserName());
        orderVo.setProductName(productInfo.getProductName());
        orderVo.setProductNum(orderInfo.getProductCount());

        return orderVo;
    }

    /**
     * 方法实现说明:模仿  流控模式【关联】  读接口
     * @author:smlz
     * @param orderNo
     * @return:
     * @exception:
     * @date:2019/11/24 22:06
     */
    @RequestMapping("/findById/{orderNo}")
    public Object findById(@PathVariable("orderNo") String orderNo) {
        log.info("orderNo:{}","执行查询操作"+System.currentTimeMillis());
        return orderInfoMapper.selectOrderInfoById(orderNo);
    }

    @RequestMapping("/getOrderById")
    public Object getOrderById(@RequestParam String orderNo) {
        log.info("orderNo:{}","执行查询操作");
        return orderInfoMapper.selectOrderInfoById(orderNo);
    }

    /**
     * 方法实现说明:模仿流控模式【关联】   写接口(优先)
     * @author:smlz
     * @return:
     * @exception:
     * @date:2019/11/24 22:07
     */
    @RequestMapping("/saveOrder")
    public String saveOrder() throws InterruptedException {
        //Thread.sleep(500);
        log.info("执行保存操作,模仿返回订单ID");
        return UUID.randomUUID().toString();
    }

    @RequestMapping("/findAll")
    public String findAll() throws InterruptedException {
        orderServiceImpl.common();
        return "findAll";
    }

    @RequestMapping("/findAllByCondtion")
    public String findAllByCondtion() {
        orderServiceImpl.common();
        return "findAllByCondition";
    }

    @RequestMapping("/test")
    public String test() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject("http://localhost:8084/test",null,String.class);
    }

}
