package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.multipart.MultipartFile;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Tulingvip04MsAlibabaSentinelOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tulingvip04MsAlibabaSentinelOrderApplication.class, args);
	}
}
