package com.tuling.interceptor;

import com.alibaba.fastjson.JSON;
import com.tuling.config.MDA;
import com.tuling.entity.TokenInfo;
import com.tuling.util.CookieUtils;
import com.tuling.vo.Result;
import com.tuling.vo.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:基于Cookie的单点登陆模式
* @author: smlz
* @createDate: 2020/1/20 20:50
* @version: 1.0
*/
@Component
@Slf4j
public class CookieLoginInterceptor implements HandlerInterceptor {

    public static final String loginUrl = "http://auth.tuling.com:8888/oauth/authorize?response_type=code&client_id=portal_app&redirect_uri=http://portal.tuling.com:8855/callBack&state=";

    @Autowired
    private RestTemplate restTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //先从cookie中读取accessToken的值
        String accessTokenInCookie = CookieUtils.readCookieValue(request, MDA.COOKIE_ACCESS_TOKEN_KEY);
        log.info("从cookie中读取AccessToken的值:{}",accessTokenInCookie);

        //从cookie中获取refreshToken的值
        String refreshTokenInCookie = CookieUtils.readCookieValue(request, MDA.COOKIE_REFRESH_TOKEN_KEY);
        log.info("从cookie中读取ReFreshToken的值:{}",refreshTokenInCookie);

        //说明token还没失效
        if(!StringUtils.isEmpty(accessTokenInCookie)) {
            response.setHeader(MDA.COOKIE_ACCESS_TOKEN_KEY,accessTokenInCookie);
            return true;
        } else { //token失效了  或者压根没有登陆过

            if(!StringUtils.isEmpty(refreshTokenInCookie)) { //说明登陆过，但是token失效了
                return dealTokenExpire(refreshTokenInCookie,request,response);
            }else { //没有登陆，或者refreshtoken失效
                return dealDiffRequest(request,response);
            }
        }

    }

    private boolean dealDiffRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = request.getRequestURL().toString();
        log.info("需要登陆的url:{}",url);

        String contentType = request.getContentType();
        //表示的ajax请求
        if(contentType!=null && contentType.contains(MediaType.APPLICATION_JSON_UTF8.toString())) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
            response.getWriter().write(JSON.toJSONString(Result.fail(SystemErrorType.REFRESH_TOKEN_EXPIRE)));
            return false;
        }


        //重定向到认证服务器
        response.sendRedirect(loginUrl+url);
        return false;
    }

    /**
     * 处理token过期逻辑,我们可以通过refresh_token去刷新我们的token  可能存在问题就是 refresh_token也过期了
     */
    private boolean dealTokenExpire(String refreshToken,HttpServletRequest request,HttpServletResponse response) throws IOException {

        //封装刷新token的请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MDA.CLIENT_ID,MDA.CLIENT_SECRET);

        //封装刷新token的参数
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("grant_type","refresh_token");
        param.add("refresh_token",refreshToken);

        //封装请求入参
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(param,httpHeaders);

        ResponseEntity<TokenInfo> responseEntity;
        try {
            //刷新我们的令牌
            responseEntity = restTemplate.exchange(MDA.AUTH_SERVER_URL, HttpMethod.POST, httpEntity, TokenInfo.class);
            TokenInfo newTokenInfo = responseEntity.getBody().initExpireTime();

            //把新的令牌存储到我们的cookie中
            CookieUtils.writeCookie(response,newTokenInfo);
            response.setHeader(MDA.COOKIE_ACCESS_TOKEN_KEY,newTokenInfo.getAccess_token());
        }catch (Exception e) {

            //refresh_token失效了 从新走认证服务器流程
            log.warn("根据refresh_token:{}获取access_token失败",refreshToken);
            return dealDiffRequest(request,response);
        }
        return true;

    }

}
