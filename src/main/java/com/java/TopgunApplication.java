package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * Spring Boot启动类
 * @author zxy
 *
 */
@SpringBootApplication
@ServletComponentScan
@EnableAutoConfiguration
public class TopgunApplication extends SpringBootServletInitializer{
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TopgunApplication.class);
	}
	

	public static void main(String[] args) {
		SpringApplication.run(TopgunApplication.class, args);
	}
	
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//
//	   return (container -> {
//	        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
//	        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
//	        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
//
//	        container.addErrorPages(error401Page, error404Page, error500Page);
//	   });
//	}
}
