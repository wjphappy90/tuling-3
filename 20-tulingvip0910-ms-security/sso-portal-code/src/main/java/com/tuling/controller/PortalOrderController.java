package com.tuling.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuling.config.MDA;
import com.tuling.entity.OrderInfo;
import com.tuling.entity.TokenInfo;
import com.tuling.util.CookieUtils;
import com.tuling.util.GetTokenUtils;
import com.tuling.vo.BuyVo;
import com.tuling.vo.OrderQo;
import com.tuling.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static com.tuling.config.MDA.ORDER_SERVER_CREATEORDER;
import static com.tuling.config.MDA.TOKEN_INFO_KEY;


/**
 * Created by smlz on 2019/12/29.
 */
@Controller
@Slf4j
public class PortalOrderController {

    @Autowired
    private RestTemplate restTemplate;

/*    @RequestMapping("/order/createOrder/{productNo}")
    public String saveOrder(@PathVariable("productNo") String productNo, HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException {

        try {
            BuyVo buyVo = new BuyVo();
            buyVo.setProductNo(productNo);
            buyVo.setProductCount(1);
            HttpEntity httpEntity = wrapRequest(request,response,buyVo);

            ResponseEntity<Result<String>> responseEntity = restTemplate.exchange(ORDER_SERVER_CREATEORDER,HttpMethod.POST,
                                            httpEntity,new ParameterizedTypeReference<Result<String>>() {});

            return "redirect:/detail/"+responseEntity.getBody().getData();
        }catch (Exception e) {
            log.warn("创建订单异常.....");
            return null;
        }

    }*/

    @RequestMapping("/order/createOrder")
    @ResponseBody
    public Result<String> saveOrder(@RequestBody BuyVo buyVo , HttpServletRequest request,HttpServletResponse response) throws JsonProcessingException {

        try {
            HttpEntity httpEntity = wrapRequest(request,response,buyVo);

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
    private HttpEntity wrapRequest(HttpServletRequest request,HttpServletResponse response,BuyVo buyVo) {

/*        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);
        String accessToken = tokenInfo.getAccess_token();*/

        //todo 拦截器中把accessToken存储到了请求头中
        String accessToken = GetTokenUtils.getAccessToken(request,response);

        SysUser sysUser = (SysUser) request.getSession().getAttribute(MDA.CURRENT_LOGIN_USER);

        OrderInfo orderInfoVo = createOrderInfo(buyVo,sysUser.getUsername());

        HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(orderInfoVo),wrapHeader(accessToken));

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
    public ModelAndView orderDetial(@PathVariable("orderNo") String orderNo, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

/*        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);
        String accessToken = tokenInfo.getAccess_token();*/

        //todo 拦截器中把accessToken存储到了请求头中
        String accessToken = GetTokenUtils.getAccessToken(request,response);

        SysUser sysUser = (SysUser) request.getSession().getAttribute(MDA.CURRENT_LOGIN_USER);

        try {

            OrderQo qo = new OrderQo();
            qo.setOrderNo(orderNo);
            qo.setUserName(sysUser.getUsername());

            HttpEntity httpEntity = new HttpEntity(JSON.toJSONString(qo) ,wrapHeader(accessToken));


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


    private HttpHeaders wrapHeader(String token) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+token);
        return headers;
    }

}
