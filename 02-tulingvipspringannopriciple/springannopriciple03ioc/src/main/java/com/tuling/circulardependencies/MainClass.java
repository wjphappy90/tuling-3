package com.tuling.circulardependencies;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by smlz on 2019/5/29.
 */
public class MainClass {

    public static void main(String[] args) {
        //创建IOC容器
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        //InstanceA instanceA = (InstanceA) ctx.getBean("instanceA");
        Person person = (Person) ctx.getBean("person");
        ctx.close();
    }
}
