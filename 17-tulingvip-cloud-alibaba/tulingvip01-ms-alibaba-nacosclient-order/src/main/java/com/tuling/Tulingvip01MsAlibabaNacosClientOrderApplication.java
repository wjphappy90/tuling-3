package com.tuling;

import com.tuling.anno.EnableRemoteCall;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableRemoteCall(scannerPackages = {"com.tuling.remote"})
public class Tulingvip01MsAlibabaNacosClientOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tulingvip01MsAlibabaNacosClientOrderApplication.class, args);
	}

}
