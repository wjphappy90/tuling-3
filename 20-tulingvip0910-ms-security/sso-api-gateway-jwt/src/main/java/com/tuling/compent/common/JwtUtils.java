package com.tuling.compent.common;

import com.google.common.collect.Maps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述 jwt 工具类
* @author: smlz
* @createDate: 2020/3/11 15:35
* @version: 1.0
*/
@Slf4j
public class JwtUtils {

    /**
     * 秘钥
     *
     */
    private final static String SECRET = "smlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlzsmlz";
    /**
     * 有效期，单位秒
     */
    private final static Long expirationTimeInSecond=3600l;

    /**
     * 方法实现说明:从jwt中解析Claims(Hashmap) 里面封装了用户的信息
     * @author:smlz
     * @param token jwt
     * @return:
     * @exception:
     * @date:2020/3/11 15:35
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("token解析错误", e);
            throw new IllegalArgumentException("Token invalided.");
        }
    }

    /**
     * 方法实现说明:获取jwt的过期时间
     * @author:smlz
     * @param token jwt
     * @return:
     * @exception:
     * @date:2020/3/11 15:36
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token)
                .getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */

    /**
     * 方法实现说明:判断jwt是否过期
     * @author:smlz
     * @param token jwt令牌
     * @return: true 表示过期,false 标识没有过期
     * @exception:
     * @date:2020/3/11 15:37
     */
    private static Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 方法实现说明:计算过期时间
     * @author:smlz
     * @return: 过期时间
     * @exception:
     * @date:2020/3/11 15:38
     */
    private static Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + expirationTimeInSecond * 1000);
    }

    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息
     * @return token
     */

    /**
     * 支持的算法详见：https://github.com/jwtk/jjwt#features
     * 方法实现说明
     * @author:smlz
     * @param claims 用户的信息
     * @return:
     * @exception:
     * @date:2020/3/11 15:40
     */
    public static String generateJwt(Map<String, Object> claims) {
        Date createdTime = new Date();
        Date expirationTime = getExpirationTime();


        byte[] keyBytes = SECRET.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder().setClaims(claims).setIssuedAt(createdTime)
                .setExpiration(expirationTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 方法实现说明 校验token
     * @author:smlz
     * @param token jwt
     * @return: 未过期返回true，否则返回false
     * @exception:
     * @date:2020/3/11 15:40
     */
    public static Boolean validateJwt(String token) {
        return !isTokenExpired(token);
    }

    public static void main(String[] args) {

        // 2.设置用户信息
        HashMap<String, Object> claims = Maps.newHashMap();
        claims.put("id", "1");
        claims.put("userName","张三");

        // 生成jwt
        String jwt = JwtUtils.generateJwt(claims);
        // 会生成类似该字符串的内容:  aaaa.bbbb.cccc
       log.info("生成的jwt:{}",jwt);

       //校验jwt
/*       Boolean validateFlag = validateJwt(jwt);
       log.info("校验jwt结果:{}",validateFlag);*/

        //异常校验jwt
        Boolean validateFlag2 = validateJwt(jwt+2);
        log.info("校验jwt结果:{}",validateFlag2);

        //解析jwt的头部信息
        String jwtHeader = jwt.split("\\.")[0];
        log.info("头部信息:{}",jwtHeader);
        byte[] header = Base64.decodeBase64(jwtHeader.getBytes());
        log.info("解密头部信息:{}",new String(header));

        String pload = jwt.split("\\.")[1];
        log.info("pload信息:{}",pload);
        byte[] jwtPload = Base64.decodeBase64(pload.getBytes());
        log.info("解密头部信息:{}",new String(jwtPload));


    }



}
