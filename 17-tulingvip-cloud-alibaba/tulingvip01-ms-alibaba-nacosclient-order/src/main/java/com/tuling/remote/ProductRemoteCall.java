package com.tuling.remote;

import com.tuling.anno.RemoteCall;
import com.tuling.entity.ProductInfo;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Created by smlz on 2020/4/2.
 */
@RemoteCall(serviceName = "product-center",path = "/product")
public interface ProductRemoteCall {

    @RequestMapping(value = "/getProduct/{productId}",method = {RequestMethod.GET})
    ProductInfo qryProductInfo1(@PathVariable long productId);

    @RequestMapping(value = "/qryProductInfo",method = {RequestMethod.GET})
    ProductInfo qryProductInfo2(@RequestParam("productId") long productId);

    @RequestMapping(value = "/qryProductInfo2",method = {RequestMethod.POST})
    ProductInfo qryProductInfo3(@RequestBody ProductInfo productInfo);
}

class test{
    public static void main(String[] args) throws NoSuchMethodException {
        Method method = ProductRemoteCall.class.getMethod("qryProductInfo2",long.class);

        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        System.out.println(requestMapping.path());
        System.out.println(requestMapping.value());





        Parameter [] parameters = method.getParameters();
        for (Parameter parameter:parameters) {
            printParameter(parameter);
        }

    }

    private static void printParameter(Parameter parameter) {
        //参数名
        System.out.println("\t\t" + parameter.getName());
        //是否在源码中隐式声明的参数名
        System.out.println("\t\t\t implicit:" + parameter.isImplicit());
        //类文件中，是否存在参数名
        System.out.println("\t\t\t namePresent:" + parameter.isNamePresent());
        //是否为虚构参数
        System.out.println("\t\t\t synthetic:" + parameter.isSynthetic());
        System.out.println("\t\t\t VarArgs:" + parameter.isVarArgs());
    }
}
