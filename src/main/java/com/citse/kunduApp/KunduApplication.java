package com.citse.kunduApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KunduApplication {

	public static void main(String[] args) {
		SpringApplication.run(KunduApplication.class, args);
	}

}
