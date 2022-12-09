package com.howie.shirojwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(value = "com.howie.shirojwt.mapper")
@ServletComponentScan("com.howie.shirojwt.filter")
public class ShiroJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiroJwtApplication.class, args);
	}
}