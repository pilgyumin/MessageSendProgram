package com.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.client.main.MessageSendProgramClient;

@SpringBootApplication
public class MessageSendProgramApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MessageSendProgramApplication.class, args);
		context.getBean(MessageSendProgramClient.class).run();
	}

}
