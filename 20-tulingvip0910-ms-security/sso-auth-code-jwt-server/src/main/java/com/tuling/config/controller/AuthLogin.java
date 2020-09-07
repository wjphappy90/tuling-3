package com.tuling.config.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.tuling.config.role.domin.TulingUser;
import com.tuling.config.role.entity.SysUser;
import com.tuling.config.role.mapper.SysUserMapper;
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
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;


@Controller
@Slf4j
public class AuthLogin {

    @Autowired
    private KeyPair keyPair;

    @Autowired
    private TokenStore tokenService;

    @Autowired
    private SysUserMapper sysUserMapper;



    @GetMapping("/login")
    public String login() {
        return "login";
    }

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
    }

/*    @GetMapping("/publickey/jwks.json")
    @ResponseBody
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }*/





    @RequestMapping("/user/getCurrentUser")
    @ResponseBody
    public SysUser me(@RequestParam("access_token") String accessToken) {
        OAuth2Authentication oAuth2Authentication = tokenService.readAuthentication(accessToken);

        String userName = (String) oAuth2Authentication.getUserAuthentication().getPrincipal();
        SysUser sysUser = sysUserMapper.findByUserName(userName);
        //清空敏感信息
        sysUser.setPassword(null);
        return sysUser;
    }


}