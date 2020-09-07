package com.tuling.util;

/**
 * Created by smlz on 2019/12/3.
 */
public class ErrorResult {

    private Integer code;

    private String msg;

    public ErrorResult(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrCode();
        this.msg = errorEnum.getErrMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
