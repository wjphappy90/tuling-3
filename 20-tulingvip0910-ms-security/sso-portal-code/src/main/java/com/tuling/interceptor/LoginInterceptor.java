package com.tuling.interceptor;

import com.alibaba.fastjson.JSON;
import com.tuling.config.MDA;
import com.tuling.entity.TokenInfo;
import com.tuling.vo.Result;
import com.tuling.vo.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 过滤session中是否有token信息(基于Session的单点登陆)
 * Created by smlz on 2019/12/29.
 */
@Slf4j
/*@Component*/
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private RestTemplate restTemplate;

    public static final String loginUrl = "http://auth.tuling.com:8888/oauth/authorize?response_type=code&client_id=portal_app&redirect_uri=http://portal.tuling.com:8855/callBack&state=";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1:从session中获取出token的信息
        HttpSession session = request.getSession();
        TokenInfo tokenInfo = (TokenInfo) session.getAttribute(MDA.TOKEN_INFO_KEY);


        if(tokenInfo == null) { //表示Session 已经过期,需要从新到认证服务器上去获取token
            return dealDiffRequest(request,response);
        }else { //表示session没过期

            log.info("portal-web中的session中的tokenInfo的有效期:{},当前时间:{}",tokenInfo.getExpireTime(), LocalDateTime.now());

            //Session 没过期  判断accessToken是否过期，若过期的话 就需要通过刷新令牌去刷新我们的accessToken
            if(tokenInfo.isExpire()) {
               return dealTokenExpire(tokenInfo,request,response);
            }
        }

        return true;
    }

    /**
     * 处理token过期逻辑,我们可以通过refresh_token去刷新我们的token  可能存在问题就是 refresh_token也过期了
     * @param tokenInfo
     */
    private boolean dealTokenExpire(TokenInfo tokenInfo,HttpServletRequest request,HttpServletResponse response) throws IOException {

        //封装刷新token的请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MDA.CLIENT_ID,MDA.CLIENT_SECRET);

        //封装刷新token的参数
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("grant_type","refresh_token");
        param.add("refresh_token",tokenInfo.getRefresh_token());

        //封装请求入参
        HttpEntity<MultiValueMap<String,String>> httpEntity = new HttpEntity<>(param,httpHeaders);

        ResponseEntity<TokenInfo> responseEntity;
        try {
            //请求认证服务器刷新token的请求
            responseEntity = restTemplate.exchange(MDA.AUTH_SERVER_URL, HttpMethod.POST, httpEntity, TokenInfo.class);
            //刷新成功  把新的token的失效时间初始化好
            TokenInfo newTokenInfo = responseEntity.getBody().initExpireTime();
            //重新把token设置到 session中
            request.getSession().setAttribute(MDA.TOKEN_INFO_KEY,newTokenInfo);
        }catch (Exception e) {

            //refresh_token失效了 从新走认证服务器流程
            log.warn("根据refresh_token:{}获取access_token失败",tokenInfo.getRefresh_token());
            return dealDiffRequest(request,response);
        }
        return true;

    }

    /**
     * 处理ajax请求 和非ajax请求
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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
}
