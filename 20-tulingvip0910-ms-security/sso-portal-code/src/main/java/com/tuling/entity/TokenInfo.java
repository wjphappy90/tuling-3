package com.tuling.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by smlz on 2019/12/29.
 */
@Data
public class TokenInfo {

    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String scope;
    private String loginUser;
    private LocalDateTime expireTime;
    private Map<String,String> additionalInfo;

    /**
     * 方法实现说明:初始化token到期时间
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/19 13:32
     */
    public TokenInfo initExpireTime() {
        this.expireTime = LocalDateTime.now().plusSeconds(expires_in);
        return this;
    }

    /**
     * 方法实现说明:判断我们的accessToken是否失效
     * @author:smlz
     * @return: true|false
     * @exception:
     * @date:2020/1/19 13:47
     */
    public boolean isExpire() {
        //表示没有过期
        return LocalDateTime.now().isAfter(expireTime);
    }

    public static void main(String[] args) throws InterruptedException {
        Long expireIn = 10L;
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(expireIn);

        Thread.sleep(15000l);
        System.out.println(LocalDateTime.now().isAfter(expireTime));

    }

}
