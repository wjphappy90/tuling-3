package com.tuling.circulardependencies;

import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2019/7/4.
 */
@Component
public class Person {

    public Person() {
        System.out.println("person 的构造方法");
    }
}
