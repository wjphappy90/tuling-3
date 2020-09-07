package com.tuling.testfactorybean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by smlz on 2019/5/20.
 */
public class MainClass {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        Object bean = ctx.getBean("carFactoryBean");
        System.out.println(bean);
       /* Object bean2 = ctx.getBean("&carFactoryBean");
        System.out.println(bean2);*/



    }
}
