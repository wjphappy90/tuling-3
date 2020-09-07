package com.tuling.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by smlz on 2020/4/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface RemoteCall {

    String serviceName();

    String path();
}
