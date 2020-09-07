package com.tuling.config.controller;

import com.tuling.config.role.domin.TulingUser;
import com.tuling.config.role.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@Slf4j
public class AuthLogin {

    @Autowired
    private TokenStore tokenService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
/*
    @RequestMapping("/exit")
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        // token can be revoked here if needed
        new SecurityContextLogoutHandler().logout(request, null, null);
        try {
            log.info("currentUrl:{}",request.getRequestURL());
            log.info("跳转页面:{}",request.getParameter("redirectUrl"));
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    @RequestMapping("/user/getCurrentUser")
    @ResponseBody
    public SysUser me(@RequestParam("access_token") String accessToken) {

        OAuth2Authentication oAuth2Authentication = tokenService.readAuthentication(accessToken);

        TulingUser tulingUser = (TulingUser) oAuth2Authentication.getUserAuthentication().getPrincipal();

        SysUser sysUser = new SysUser();
        sysUser.setId(tulingUser.getUserId());
        sysUser.setEmail(tulingUser.getEmail());
        sysUser.setUsername(tulingUser.getUsername());
        sysUser.setNickname(tulingUser.getNickName());
        return sysUser;
    }


}