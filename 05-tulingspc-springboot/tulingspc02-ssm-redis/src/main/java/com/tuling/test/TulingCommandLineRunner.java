package com.tuling.test;

import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2019/10/9.
 */
@Component
public class TulingCommandLineRunner implements ApplicationRunner,ApplicationContextAware {


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("====================="+applicationContext);
    }
}
