package com.tuling.entity;

import lombok.Data;

/**
 * Created by smlz on 2019/12/29.
 */
@Data
public class TokenInfo {

    private String access_token;

    private String token_type;

    private String expires_in;

    private String scope;

    private String loginUser;
}
