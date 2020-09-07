package com.tuling;

import com.tuling.config.MyBatisConfig;
import com.tuling.mapper.EmployeeMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by smlz on 2019/8/20.
 */
public class MainStarter {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyBatisConfig.class);
        EmployeeMapper employeeMapper = context.getBean(EmployeeMapper.class);
        System.out.println(employeeMapper.list());
        System.out.println(employeeMapper.list());
    }
}
