package com.tuling.compent.filter;

import com.tuling.compent.common.MDA;
import com.tuling.compent.common.TokenInfo;
import com.tuling.compent.common.TulingRestTemplate;
import com.tuling.compent.exception.GateWayException;
import com.tuling.vo.SystemErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
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

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * 认证过滤器,根据url判断用户请求是要经过认证 才能访问
 * Created by smlz on 2019/12/17.
 */
@Component
@Slf4j
public class AuthorizationFilter implements GlobalFilter,Ordered,InitializingBean {

    @Autowired
    private TulingRestTemplate restTemplate;

    private PublicKey publicKey;

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

        //交易我们的jwt 若jwt不对或者超时都会抛出异常
        Claims claims = validateJwtToken(authHeader);

        //向headers中放文件，记得build
            ServerHttpRequest request = exchange.getRequest().mutate().header("username",claims.get("user_name").toString()).build();
        //将现在的request 变成 change对象
        ServerWebExchange serverWebExchange = exchange.mutate().request(request).build();

        //从jwt中解析出权限集合进行判断
        hasPremisson(claims,reqPath);

        return chain.filter(serverWebExchange);

    }

    private Claims validateJwtToken(String authHeader) {
        String token =null ;
        try{
             token = StringUtils.substringAfter(authHeader, "bearer ");

            Jwt<JwsHeader, Claims> parseClaimsJwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);

            Claims claims = parseClaimsJwt.getBody();
            log.info("claims:{}",claims);
            return claims;
        }catch(Exception e){
            log.error("校验token异常:{},异常信息:{}",token,e.getMessage());
            throw new GateWayException(SystemErrorType.INVALID_TOKEN);
        }
    }

    private boolean hasPremisson(Claims claims,String currentUrl) {
        boolean hasPremisson = false;
        //登陆用户的权限集合判断
        List<String> premessionList = claims.get("authorities",List.class);
        for (String url: premessionList) {
            if(currentUrl.contains(url)) {
                hasPremisson = true;
                break;
            }
        }
        if(!hasPremisson){
            log.warn("权限不足");
            throw new GateWayException(SystemErrorType.FORBIDDEN);
        }

        return hasPremisson;
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
        shouldSkipUrl.add("/oauth/token");
        shouldSkipUrl.add("/oauth/check_token");
        shouldSkipUrl.add("/user/getCurrentUser");

        //初始化公钥
        this.publicKey = genPublicKeyByTokenKey();
    }

    /**
     * 方法实现说明:去认证服务器上获取tokenKey
     * @return:
     * @exception:
     * @date:2020/1/21 16:53
     */
    private String getTokenKey(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(MDA.clientId,MDA.clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);

        try {

            ResponseEntity<Map> response = restTemplate.exchange(MDA.getTokenKey, HttpMethod.GET, entity, Map.class);

            String tokenKey = response.getBody().get("value").toString();

            log.info("去认证服务器获取TokenKey:{}",tokenKey);

            return tokenKey;
        }catch (Exception e) {

            log.error("远程调用认证服务器获取tokenKey失败:{}",e.getMessage());
            throw new GateWayException(SystemErrorType.GET_TOKEN_KEY_ERROR);
        }
    }

    private PublicKey genPublicKeyByTokenKey() {

        try{
            String tokenKey = getTokenKey();

            String dealTokenKey =tokenKey.replaceAll("\\-*BEGIN PUBLIC KEY\\-*", "").replaceAll("\\-*END PUBLIC KEY\\-*", "").trim();

            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(dealTokenKey));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);

            log.info("生成公钥:{}",publicKey);

            return publicKey;
        }catch (Exception e) {
            log.info("生成公钥异常:{}",e.getMessage());
            throw new GateWayException(SystemErrorType.GET_TOKEN_KEY_ERROR);
        }

    }


}
