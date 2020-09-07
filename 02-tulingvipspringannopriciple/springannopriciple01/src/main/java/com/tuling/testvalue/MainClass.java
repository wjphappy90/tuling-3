package com.tuling.testvalue;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Created by smlz on 2019/5/24.
 */
public class MainClass {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        System.out.println(ctx.getBean(Person.class));
        System.out.println(ctx.getBean(RedisCfgProperties.class));
    }
}
