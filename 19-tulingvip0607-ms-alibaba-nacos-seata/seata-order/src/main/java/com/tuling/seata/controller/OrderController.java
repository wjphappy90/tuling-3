package com.tuling.seata.controller;


import com.tuling.seata.domin.Order;
import com.tuling.seata.service.IOrderService;
import com.tuling.seata.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 创建订单
     */
    @RequestMapping("/create")
    public ResultVo create(Order order) {
        orderService.createOrder(order);

        ResultVo resultVo = new ResultVo();
        resultVo.setMsg("创建订单成功");
        resultVo.setSuccess(true);
        resultVo.setData("订单ID:"+order.getId());
        return resultVo;
    }

    @RequestMapping("/selectOrderById/{id}")
    public Order selectOrderById(@PathVariable("id") Integer id) {

        return orderService.selectOrderById(id);
    }
}

