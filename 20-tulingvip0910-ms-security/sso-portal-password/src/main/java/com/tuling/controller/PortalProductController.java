package com.tuling.controller;

import com.tuling.config.MDA;
import com.tuling.entity.ProductInfo;
import com.tuling.entity.TokenInfo;
import com.tuling.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.tuling.config.MDA.TOKEN_INFO_KEY;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:portal-web的商品
* @author: smlz
* @createDate: 2020/1/13 11:19
* @version: 1.0
*/
@Controller
@Slf4j
public class PortalProductController {


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/product/{id}")
    public ModelAndView showProductDetail(@PathVariable("id")Long id, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();

        //获取token
        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);

        try {

            ResponseEntity<Result<ProductInfo>> responseEntity = restTemplate.exchange(MDA.GET_PRODUCT_INFO+id,HttpMethod.GET,
                    warpRequest(tokenInfo),
                    new ParameterizedTypeReference<Result<ProductInfo>>() {});
            Result<ProductInfo> productInfoResult = responseEntity.getBody();
            log.info("根据商品id:{}查询商品详细信息:{}",id,productInfoResult.getData());
            mv.addObject("productInfo",productInfoResult.getData());
            mv.addObject("loginUser",tokenInfo.getLoginUser());
            mv.setViewName("product_detail");

        }catch (Exception e) {
            log.error("根据商品id查询商品详情异常:{}",id);
        }
        return mv;
    }

    private HttpEntity warpRequest(TokenInfo tokenInfo) {
        //设置请求头
        String token = tokenInfo.getAccess_token();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+token);
        HttpEntity httpEntity = new HttpEntity(null,headers);
        return httpEntity;
    }
}
