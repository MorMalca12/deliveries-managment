package com.dropit.deliveriesmanagment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableAsync
public class DeliveriesManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveriesManagementApplication.class, args);

	}

}
