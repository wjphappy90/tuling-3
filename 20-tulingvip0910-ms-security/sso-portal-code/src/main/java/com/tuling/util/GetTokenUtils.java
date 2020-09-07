package com.tuling.util;

import com.tuling.config.MDA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by smlz on 2020/1/20.
 */
@Slf4j
public class GetTokenUtils {

    public static String getAccessToken(HttpServletRequest request, HttpServletResponse response) {
        //先去cookie中找
        String accessTokenInCookie = CookieUtils.readCookieValue(request, MDA.COOKIE_ACCESS_TOKEN_KEY);
        if(!StringUtils.isEmpty(accessTokenInCookie)) {
            log.info("从cookie中获取到accessToken:{}",accessTokenInCookie);
            return accessTokenInCookie;
        }

        String accessTokenInHeader = response.getHeader(MDA.COOKIE_ACCESS_TOKEN_KEY);
        if(!StringUtils.isEmpty(accessTokenInHeader)) {
            log.info("从Header中读取到accessToken:{}",accessTokenInHeader);
            return accessTokenInHeader;
        }

        return null;
    }
}
