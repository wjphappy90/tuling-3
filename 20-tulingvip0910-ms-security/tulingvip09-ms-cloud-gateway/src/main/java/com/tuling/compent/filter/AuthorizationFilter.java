package com.tuling.compent.filter;

import com.tuling.compent.common.MDA;
import com.tuling.compent.common.TokenInfo;
import com.tuling.compent.exception.SystemErrorType;
import com.tuling.compent.exception.GateWayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 认证过滤器,根据url判断用户请求是要经过认证 才能访问
 * Created by smlz on 2019/12/17.
 */
@Component
@Slf4j
public class AuthorizationFilter implements GlobalFilter,Ordered,InitializingBean {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 请求各个微服务 不需要用户认证的URL
     */
    private static Set<String> shouldSkipUrl = new LinkedHashSet<>();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String reqPath = exchange.getRequest().getURI().getPath();
        log.info("网关认证开始URL->:{}",reqPath);

        //1:不需要认证的url
        if(shouldSkip(reqPath)) {
            log.info("无需认证的路径");
            return chain.filter(exchange);
        }

        //获取请求头
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        //请求头为空
        if(StringUtils.isEmpty(authHeader)) {
            log.warn("需要认证的url,请求头为空");
            throw new GateWayException(SystemErrorType.UNAUTHORIZED_HEADER_IS_EMPTY);
        }

        TokenInfo tokenInfo=null;
        try {
            //获取token信息
            tokenInfo = getTokenInfo(authHeader);
        }catch (Exception e) {
            log.warn("校验令牌异常:{}",authHeader);
            throw new GateWayException(SystemErrorType.INVALID_TOKEN);
        }

        //向headers中放文件，记得build
        ServerHttpRequest request = exchange.getRequest().mutate().header("userName", tokenInfo.getUser_name()).build();
        //将现在的request 变成 change对象
        ServerWebExchange serverWebExchange = exchange.mutate().request(request).build();

        serverWebExchange.getAttributes().put("tokenInfo",tokenInfo);

        return chain.filter(serverWebExchange);

    }

    private TokenInfo getTokenInfo(String authHeader) {
        String token = StringUtils.substringAfter(authHeader, "bearer ");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(MDA.clientId, MDA.clientSecret);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(MDA.checkTokenUrl, HttpMethod.POST, entity, TokenInfo.class);

        log.info("token info :" + response.getBody().toString());

        return response.getBody();
    }



    /**
     * 方法实现说明:不需要授权的路径
     * @author:smlz
     * @param reqPath 当前请求路径
     * @return:
     * @exception:
     * @date:2019/12/26 13:49
     */
    private boolean shouldSkip(String reqPath) {

        for(String skipPath:shouldSkipUrl) {
            if(reqPath.contains(skipPath)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         *实际上，这边需要通过去数据库读取 不需要认证的URL,不需要认证的URL是各个微服务
         * 开发模块的人员提供出来的. 我在这里没有去查询数据库了,直接模拟写死
         */
        //模仿商品详情接口不需要认证
        shouldSkipUrl.add("/product/selectProductInfoById");
        //去认证的请求,本来就不需要拦截
        shouldSkipUrl.add("/oauth/token");
    }


}
