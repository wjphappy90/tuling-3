package com.tuling.seata.domin;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
public class Order implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键Id
     */
    private Integer id;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 付款金额
     */
    private BigDecimal payMoney;

    /**
     * 商品Id
     */
    private Integer productId;

    /**
     * 状态 0下单 1完成
     */
    private Integer status;

    /**
     * 商品数量
     */
    private Integer count;


}
