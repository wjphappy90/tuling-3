package com.tuling.compent.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),

    GATEWAY_ERROR("010500", "网关异常"),

    FORBIDDEN("403","无权访问"),

    TOKEN_TIMEOUT("403-1","token过期"),

    INVALID_TOKEN("401-2", "无效token"),


    UNAUTHORIZED_HEADER_IS_EMPTY("401-1","无权访问,请求头为空");

    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }
}
