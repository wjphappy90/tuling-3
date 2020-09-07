package com.tuling.controller;

import com.tuling.config.MDA;
import com.tuling.entity.TokenInfo;
import com.tuling.util.CookieUtils;
import com.tuling.vo.Result;
import com.tuling.vo.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by smlz on 2020/1/12.
 */
@Controller
@Slf4j
public class CallBackController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/callBack")
    public String callBack(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletRequest request,
                           HttpServletResponse httpServletResponse) {

        log.info("code:{}",code);
        log.info("state:{}",state);

        //通过code获取token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MDA.CLIENT_ID,MDA.CLIENT_SECRET);

        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("code",code);
        param.add("grant_type","authorization_code");
        param.add("redirect_uri",MDA.CALL_BACK_URL);

        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(param,httpHeaders);

        ResponseEntity<TokenInfo> response =null;
        try {
            response = restTemplate.exchange(MDA.AUTH_SERVER_URL, HttpMethod.POST, httpEntity, TokenInfo.class);
        }catch (Exception e) {
            log.warn("通过授权码获取token失败:{}",code);
        }


        //把token存放到session中
        TokenInfo tokenInfo = response.getBody();
        log.info("tokenInfo:{}",tokenInfo);

        //session的登陆方案
        /*request.getSession().setAttribute(MDA.TOKEN_INFO_KEY,tokenInfo.initExpireTime());*/
        //request.getSession().setMaxInactiveInterval(10);


        //todo cookie的登陆方案
        CookieUtils.writeCookie(httpServletResponse,tokenInfo);


        //根据accessToken获取用户登陆信息
        String accessToken = tokenInfo.getAccess_token();
        HttpEntity<MultiValueMap<String,String>> userEntity = new HttpEntity<>(null,httpHeaders);

        ResponseEntity<SysUser> userResponseEntity =null;
        try {
            userResponseEntity = restTemplate.exchange(MDA.GET_CURRENT_USER+accessToken, HttpMethod.POST, userEntity, SysUser.class);
        }catch (Exception e) {
            log.warn("通过授权码获取token失败:{},异常:{}",code,e);
        }

        SysUser sysUser = userResponseEntity.getBody();
        log.info("当前登陆的用户:{}",sysUser);

        //把登陆用户存储到session中
        request.getSession().setAttribute(MDA.CURRENT_LOGIN_USER,sysUser);

        return "redirect:"+state;
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();

        return Result.success(SystemErrorType.LOGOUT_SUCCESS);
    }
}
