package com.tuling.handler;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:解决RestFule风格的请求
 *       eg:    /selectOrderInfoById/2     /selectOrderInfoById/1 需要转为/selectOrderInfoById/{number}
* @author: smlz
* @createDate: 2019/12/4 13:28
* @version: 1.0
*/
@Component
@Slf4j
public class TulingUrlClean implements UrlCleaner {
    @Override
    public String clean(String originUrl) {
        log.info("originUrl:{}",originUrl);

        if(StringUtils.isEmpty(originUrl)) {
            log.error("originUrl not be null");
            throw new IllegalArgumentException("originUrl not be null");
        }
        return replaceRestfulUrl(originUrl);
    }

    /**
     * 方法实现说明:把/selectOrderInfoById/2 替换成/selectOrderInfoById/{number}
     * @author:smlz
     * @param sourceUrl 目标url
     * @return: 替换后的url
     * @exception:
     * @date:2019/12/4 13:46
     */
    private String replaceRestfulUrl(String sourceUrl) {
        List<String> origins = Arrays.asList(sourceUrl.split("/"));
        StringBuffer targetUrl = new StringBuffer("/");

        for(String str:origins) {
            if(NumberUtils.isNumber(str)) {
                targetUrl.append("/{number}");
            }else {
                targetUrl.append(str);
            }

        }
        return targetUrl.toString();
    }
}
