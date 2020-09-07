package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Tulingvip02MsAlibabaRibbonOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tulingvip02MsAlibabaRibbonOrderApplication.class, args);
	}

}
