package com.tuling.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * 用于切DataSource中getConnection()方法的
 * Created by smlz on 2019/7/24.
 */
@Aspect
@Component
public class AngleConnectionAspect {


    /**
     * 环绕通知
     * @param proceedingJoinPoint 连接点
     * @return
     */
    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection cutConnectionMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //返回数据库原生的Connection
        Connection connection = (Connection) proceedingJoinPoint.proceed();
        return connection;
    }
}
