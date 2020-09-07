package com.tuling;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(exclude = {
				GlobalTransactionAutoConfiguration.class,
				DataSourceAutoConfiguration.class
})
public class SeataProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeataProductApplication.class, args);
	}

}
