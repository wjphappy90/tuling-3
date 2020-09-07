package com.tuling.handler;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:区分来源接口
* @author: smlz
* @createDate: 2019/12/4 13:13
* @version: 1.0
*/
/*@Component*/
@Slf4j
public class TulingRequestOriginParse implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        String origin = request.getHeader("origin");
        if(StringUtils.isEmpty(origin)) {
            log.warn("origin must not null");
            throw new IllegalArgumentException("request origin must not null");
        }
        return origin;
    }
}
