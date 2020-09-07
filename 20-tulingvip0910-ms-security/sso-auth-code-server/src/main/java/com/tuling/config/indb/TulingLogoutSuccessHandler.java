package com.tuling.config.indb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by smlz on 2020/1/19.
 */
@Component
@Slf4j
public class TulingLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("currentUrl:{}",request.getRequestURL());
        log.info("跳转页面:{}",request.getParameter("redirectUrl"));
        String redirectUrl = request.getParameter("redirectUrl");
        if(!StringUtils.isEmpty(redirectUrl)) {
            response.sendRedirect(redirectUrl);
        }
    }
}
