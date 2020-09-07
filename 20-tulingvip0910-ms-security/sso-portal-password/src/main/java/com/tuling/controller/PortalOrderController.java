package com.tuling.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuling.config.MDA;
import com.tuling.entity.OrderInfo;
import com.tuling.entity.TokenInfo;
import com.tuling.vo.BuyVo;
import com.tuling.vo.OrderQo;
import com.tuling.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.tuling.config.MDA.*;


/**
 * Created by smlz on 2019/12/29.
 */
@Controller
@Slf4j
public class PortalOrderController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/order/createOrder")
    @ResponseBody
    public Result<String> saveOrder(@RequestBody BuyVo buyVo, HttpServletRequest request) throws JsonProcessingException {

        try {
            HttpEntity httpEntity = wrapRequest(request,buyVo);

            ResponseEntity<Result<String>> responseEntity = restTemplate.exchange(ORDER_SERVER_CREATEORDER,HttpMethod.POST,
                                            httpEntity,new ParameterizedTypeReference<Result<String>>() {});

            return responseEntity.getBody();
        }catch (Exception e) {
            log.warn("创建订单异常.....");
            return Result.fail();
        }


    }

    /**
     * 方法实现说明:包装请求头
     * @author:smlz
     * @param request
     * @param buyVo
     * @return: HttpEntity
     * @exception:
     * @date:2020/1/13 13:11
     */
    private HttpEntity wrapRequest(HttpServletRequest request,BuyVo buyVo) {

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);

        OrderInfo orderInfoVo = createOrderInfo(buyVo,tokenInfo.getLoginUser());

        HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(orderInfoVo),wrapHeader(tokenInfo));

        return httpEntity;

    }


    private OrderInfo createOrderInfo(BuyVo buyVo,String LoginUserName) {
        OrderInfo orderInfoVo = new OrderInfo();
        orderInfoVo.setProductNo(buyVo.getProductNo());
        orderInfoVo.setUserName(LoginUserName);
        orderInfoVo.setProductCount(buyVo.getProductCount());
        orderInfoVo.setCreateDt(new Date());
        return orderInfoVo;
    }

    @RequestMapping("/detail/{orderNo}")
    public ModelAndView orderDetial(@PathVariable("orderNo") String orderNo, HttpServletRequest request) throws JsonProcessingException {

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);

        try {

            OrderQo qo = new OrderQo();
            qo.setOrderNo(orderNo);
            qo.setUserName(tokenInfo.getLoginUser());

            HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(qo) ,wrapHeader(tokenInfo));


            Result<OrderInfo> orderInfoResult = restTemplate.exchange(MDA.ORDER_SERVER_DETAIL,HttpMethod.POST,
                    httpEntity,new ParameterizedTypeReference<Result<OrderInfo>>() {}).getBody();
            ModelAndView modelAndView = new ModelAndView("order_detail");
            modelAndView.addObject("orderInfo",orderInfoResult.getData());
            return modelAndView;
        }catch (Exception e) {
            log.error("查询订单异常:{},{}",orderNo,e);
        }
        return null;
    }


    private HttpHeaders wrapHeader(TokenInfo tokenInfo) {
        //设置请求头
        String token = tokenInfo.getAccess_token();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+token);
        return headers;
    }

}
