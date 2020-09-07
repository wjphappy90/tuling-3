package com.tuling.exceptionhandler;

import com.tuling.vo.OrderVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by smlz on 2019/11/24.
 */
@ControllerAdvice
public class TulingExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseBody
    public Object dealBizException() {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo("-1");
        orderVo.setUserName("容错用户");
        return orderVo;
    }
}
