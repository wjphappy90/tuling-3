package com.tuling;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication(excludeName = {"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"})
@MapperScan(value = {"com.tuling.mapper"})
public class Tulingspc02SpringbootMybatisApplication {

	public static void main(String[] args) throws ClassNotFoundException {
		SpringApplication.run(Tulingspc02SpringbootMybatisApplication.class, args);

	}



}
