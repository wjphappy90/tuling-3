package com.tuling.v1.controller;

import com.tuling.entity.OrderInfo;
import com.tuling.entity.PayInfo;
import com.tuling.entity.ProductInfo;
import com.tuling.feignapi.paycenter.PayCenterFeignApi;
import com.tuling.feignapi.productcenter.ProductCenterFeignApi;
import com.tuling.feignapi.sentinel.ProductCenterFeignApiWithSentinel;
import com.tuling.mapper.OrderInfoMapper;
import com.tuling.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by smlz on 2019/11/17.
 */
@RestController
public class OrderInfoController {

    @Autowired
    private ProductCenterFeignApiWithSentinel productCenterFeignApi;

    @Autowired
    private PayCenterFeignApi payCenterFeignApi;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @RequestMapping("/selectOrderInfoById/{orderNo}")
    public Object selectOrderInfoById(@PathVariable("orderNo") String orderNo) throws InterruptedException {

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

    @RequestMapping("/testFeignInterceptor")
    public String testFeignInterceptor() {
        /*return productCenterFeignApi.getToken4Header();*/
        return null;
    }

    @RequestMapping("/getPayInfoByOrderNo/{orderNo}")
    public PayInfo selectPayInfoByOrderNo(@PathVariable("orderNo") String orderNo) {
        return payCenterFeignApi.selectPayInfoByOrderNo(orderNo);
    }



}
