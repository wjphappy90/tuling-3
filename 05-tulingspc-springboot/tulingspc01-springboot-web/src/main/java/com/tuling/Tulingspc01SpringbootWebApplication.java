package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class Tulingspc01SpringbootWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(Tulingspc01SpringbootWebApplication.class, args);
	}

}
