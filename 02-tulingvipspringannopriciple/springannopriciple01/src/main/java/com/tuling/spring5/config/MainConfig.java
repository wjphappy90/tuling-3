package com.tuling.spring5.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by smlz on 2019/7/7.
 */
@Configuration
@ImportResource(value = {"classpath:beans.xml"})
public class MainConfig {

/*	@Bean
	public TulingDao tulingDao() {

		return new TulingDao(tulingDataSource());
	}

	@Bean
	public TulingDataSource tulingDataSource() {
		return new TulingDataSource();
	}*/
}
