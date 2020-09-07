package com.tuling.core;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

import java.lang.reflect.Proxy;

/**
 * Created by smlz on 2020/4/2.
 */
public class RemoteCallFactoryBean<T> extends AbstractDiscoveryClientSupport implements FactoryBean<T>  {

    private Class<T> targetClass;

    public RemoteCallFactoryBean(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Nullable
    @Override
    public T getObject() throws Exception {

/*        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        };*/

        RemoteCallProxy remoteCallProxy = new RemoteCallProxy(getDiscoveryClient());

        return (T) Proxy.newProxyInstance(targetClass.getClassLoader(),
                new Class<?>[] { targetClass },
                remoteCallProxy);
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return targetClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
