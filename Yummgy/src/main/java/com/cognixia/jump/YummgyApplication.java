package com.cognixia.jump;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition (
		info = @Info(title="Yumggy App API", version="1.0",
			description="An app that allows users to add their own recipes, view other user's recipes,"
					+ " favorite those recipes, and keep track of their favorites.",
			contact=@Contact(name="Grant Smith", email="bomono8@gmail.com"),
			license=@License(name="Food Co")
		)
	)
public class YummgyApplication {

	public static void main(String[] args) {
		SpringApplication.run(YummgyApplication.class, args);
	}

}
