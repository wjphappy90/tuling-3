package com.tuling.interceptor;

import com.tuling.config.MDA;
import com.tuling.entity.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:拦截器,拦截portal-web工程中需要登陆的url
* @author: smlz
* @createDate: 2020/1/13 10:32
* @version: 1.0
*/
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从session中获取出token的信息
        HttpSession session = request.getSession();

        TokenInfo tokenInfo = (TokenInfo) session.getAttribute(MDA.TOKEN_INFO_KEY);

        if( tokenInfo == null) {

            String url = request.getRequestURL().toString();

            log.info("需要登陆的url:{}",url);
            //重定向到登陆页面
            response.sendRedirect("/login.html?targetUrl="+url);
            return false;
        }

        return true;
    }
}
