package com.tuling.controller;

import com.tuling.config.MDA;
import com.tuling.entity.ProductInfo;
import com.tuling.entity.TokenInfo;
import com.tuling.util.CookieUtils;
import com.tuling.util.GetTokenUtils;
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
import javax.servlet.http.HttpServletResponse;

import static com.tuling.config.MDA.TOKEN_INFO_KEY;

/**
 * Created by smlz on 2019/12/29.
 */
@Controller
@Slf4j
public class PortalProductController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/product/{id}")
    public ModelAndView showProductDetail(@PathVariable("id")Long id, HttpServletRequest request, HttpServletResponse response) {

        ModelAndView mv = new ModelAndView();

        //Session中获取accessToken
/*        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute(TOKEN_INFO_KEY);
        String accessToken = tokenInfo.getAccess_token();*/


        //todo 拦截器中把accessToken存储到了请求头中
        String accessToken = GetTokenUtils.getAccessToken(request,response);



        SysUser sysUser = (SysUser) request.getSession().getAttribute(MDA.CURRENT_LOGIN_USER);

        try {

            ResponseEntity<Result<ProductInfo>> responseEntity = restTemplate.exchange(MDA.GET_PRODUCT_INFO+id, HttpMethod.GET,
                    warpRequest(accessToken),
                    new ParameterizedTypeReference<Result<ProductInfo>>() {});
            Result<ProductInfo> productInfoResult = responseEntity.getBody();
            log.info("根据商品id:{}查询商品详细信息:{}",id,productInfoResult.getData());
            mv.addObject("productInfo",productInfoResult.getData());
            mv.addObject("loginUser",sysUser.getUsername());
            mv.setViewName("product_detail");

        }catch (Exception e) {
            log.error("根据商品id查询商品详情异常:{}",id);
        }
        return mv;
    }

    private HttpEntity warpRequest(String token) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","bearer "+token);
        HttpEntity httpEntity = new HttpEntity(null,headers);
        return httpEntity;
    }
}
