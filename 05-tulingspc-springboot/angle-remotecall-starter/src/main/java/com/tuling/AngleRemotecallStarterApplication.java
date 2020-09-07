package com.tuling;

import com.tuling.anno.EnableRemoteCall;
import com.tuling.remote.ProductRemoteCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableRemoteCall(scannerPackages = {"com.tuling.remote"})
@RestController
public class AngleRemotecallStarterApplication {

	@Autowired
	private ProductRemoteCall productRemoteCall;

	public static void main(String[] args) {
		SpringApplication.run(AngleRemotecallStarterApplication.class, args);
	}

	@RequestMapping("/test")
	public void test() {
		System.out.println(productRemoteCall);
	}

}
