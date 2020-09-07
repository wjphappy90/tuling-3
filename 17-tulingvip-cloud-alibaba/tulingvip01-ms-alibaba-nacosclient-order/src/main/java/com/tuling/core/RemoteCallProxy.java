package com.tuling.core;

import com.tuling.anno.RemoteCall;
import com.tuling.rule.RandomChooseInstanceRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by smlz on 2020/4/2.
 */
@Slf4j
public class RemoteCallProxy implements InvocationHandler {

    private DiscoveryClient discoveryClient;

    private Class targetClass;

    private static Map<String,List<ServiceInstance>> allServerList = new ConcurrentHashMap<>();

    public RemoteCallProxy(DiscoveryClient discoveryClient,Class targetClass) {
        this.discoveryClient = discoveryClient;
        this.targetClass =targetClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取解析的RemoteCall注解
        RemoteCall remoteCallAnno = (RemoteCall) targetClass.getAnnotation(RemoteCall.class); // 取得指定注释
        String serviceName = remoteCallAnno.serviceName();
        String reqSuffix = remoteCallAnno.path();
        //获取全部的服务列表
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceName);

        //todo 这里可以做扩展 针对不同的服务使用不同的负载均衡算法 这里的demo我就是用随机的
        ServiceInstance serviceInstance =new RandomChooseInstanceRule().chooseInstance(serviceInstanceList);

        //todo 这里只支持解析@RequestMapping注解
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

        //todo 这里只支持单个映射  需要的自己去扩展
        String requestPath = requestMapping.value()[0];

        log.info("{},{},{}:",serviceInstance.getHost(),serviceInstance.getPort(),serviceInstance.getUri());
        String realUrl = genReqUrl(serviceInstance.getUri(),reqSuffix,requestPath,method,args);


        //todo  老师这这里 只做了demo级别的演示 真是生产 比这里复杂的多
        RestTemplate restTemplate = new RestTemplate();
        if(requestMapping.method()[0].equals(RequestMethod.GET) && null!= method.getParameters()[0].getAnnotation(RequestParam.class)) {
            String argsName = method.getParameters()[0].getAnnotation(RequestParam.class).value();
            return restTemplate.getForObject(realUrl+"?"+argsName+"="+args[0],method.getReturnType());
        }else if(requestMapping.method()[0].equals(RequestMethod.POST)){
            return restTemplate.postForObject(realUrl,args[0],method.getReturnType());
        }else if(requestMapping.method()[0].equals(RequestMethod.GET) && null!= method.getParameters()[0].getAnnotation(PathVariable.class)) {
            return restTemplate.getForObject(realUrl,method.getReturnType());
        }
        return null;
    }

    private String genReqUrl(URI uri,String reqSuffix,String reqPath,Method method,Object[] args) {

        StringBuilder reqUri = new StringBuilder(uri.toString());

        if(!StringUtils.isEmpty(reqSuffix)) {
            reqUri.append(reqSuffix);
        }

        for (Parameter parameter:method.getParameters()) {
            PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
            if(pathVariable == null) {
                reqUri.append(reqPath);
            }else {//需要替换/getProduct/{productId} 为对应的/getProduct/1
                reqUri.append(urlClear(reqPath)+args[0]);
            }
        }

        return reqUri.toString();
    }

    private String urlClear(String targetUrl) {
        Integer beginIndex = targetUrl.indexOf("{");
        return targetUrl.substring(0,beginIndex);
    }
}
