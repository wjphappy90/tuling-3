package com.tuling.v1.controller;

import com.tuling.entity.PayInfo;
import com.tuling.mapper.PayInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by smlz on 2019/11/17.
 */
@RestController
@Slf4j
public class PayInfoController {


    @Autowired
    private PayInfoMapper payInfoMapper;

    @RequestMapping("/selectPayInfoByOrderNo/{orderNo}")
    public PayInfo selectPayInfoByOrderNo(@PathVariable("orderNo") String orderNo) {
        return payInfoMapper.selectPayInfoByOrderNo(orderNo);
    }

    @RequestMapping("/save")
    public String savePayInfo(@RequestBody PayInfo payInfo) {
        log.info("payInfo:{}",payInfo);
        return payInfo.getOrderNo();
    }
}
