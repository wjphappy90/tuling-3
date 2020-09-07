package com.tuling.util;

import com.tuling.config.MDA;
import com.tuling.entity.TokenInfo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:cookie工具
* @author: smlz
* @createDate: 2020/1/19 20:00
* @version: 1.0
*/
public class CookieUtils {


    /**
     * 方法实现说明:写cookie
     * @author:smlz
     * @param response
     * @param tokenInfo
     * @return:
     * @exception:
     * @date:2020/1/19 20:01
     */
    public static void writeCookie(HttpServletResponse response, TokenInfo tokenInfo) {
        Cookie accessCookie = new Cookie(MDA.COOKIE_ACCESS_TOKEN_KEY,tokenInfo.getAccess_token());
        accessCookie.setMaxAge(3600);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);
        accessCookie.setDomain("tuling.com");
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie(MDA.COOKIE_REFRESH_TOKEN_KEY,tokenInfo.getRefresh_token());
        refreshCookie.setMaxAge(3600*24*30);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(false);
        refreshCookie.setDomain("tuling.com");
        response.addCookie(refreshCookie);
    }


    public static String readCookieValue(HttpServletRequest request,String cookieKey) {
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {

            for(Cookie cookie: cookies) {
                if(cookie.getName().equals(cookieKey)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
