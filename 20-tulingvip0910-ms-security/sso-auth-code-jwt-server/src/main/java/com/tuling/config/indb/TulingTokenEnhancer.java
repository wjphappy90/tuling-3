package com.tuling.config.indb;

import com.tuling.config.role.domin.TulingUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * token增强器,根据自己业务 往token存储业务字段
 * Created by smlz on 2020/1/20.
 */

public class TulingTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        TulingUser tulingUser = (TulingUser) authentication.getPrincipal();

        final Map<String, Object> additionalInfo = new HashMap<>();
        final Map<String, Object> retMap = new HashMap<>();

        additionalInfo.put("email",tulingUser.getEmail());
        additionalInfo.put("phone",tulingUser.getPhone());
        additionalInfo.put("userId",tulingUser.getUserId());
        additionalInfo.put("nickName",tulingUser.getNickName());

        retMap.put("additionalInfo",additionalInfo);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(retMap);

        return accessToken;
    }
}
