package com.tuling.core;

import org.omg.CORBA.portable.InvokeHandler;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by smlz on 2020/4/2.
 */
public class RemoteCallProxy implements InvocationHandler {

    private DiscoveryClient discoveryClient;

    public RemoteCallProxy(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
