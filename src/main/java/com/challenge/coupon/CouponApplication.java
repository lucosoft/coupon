package com.challenge.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootApplication
public class CouponApplication {

	private final static Logger logger = Logger.getLogger("com.challenge.coupon.CouponApplication");

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}


	public static void main(String[] args) {
		SpringApplication.run(CouponApplication.class, args);
		logger.info(() -> "Coupon Application is now running.....");
	}
}
