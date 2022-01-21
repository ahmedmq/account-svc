package com.trainingdemo.accountsvc.controller;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

//@Configuration
public class FunctionDemo {

	// Function Functional Interface
	// Takes a string as input, convert it into uppercase and return the value
	// Convention over configuration
	// Input Topic -> <function_name>-in-0 = process-in-0
	// Output Topic -> <function_name>-out-0 = process-out-0
	@Bean
	Function<Message<Person>, String> process(StreamBridge streamBridge){
		return v -> {
			System.out.println("Converting message to uppercase: "+v.getPayload().getName());
			System.out.println("Message Header value: "+v.getHeaders().get("Type"));

			System.out.println("Sending message using Stream Bridge");
			streamBridge.send("spring-queue", v.getPayload().getName());

			return v.getPayload().getName().toUpperCase();
		};
	}

	public static class Person {
		String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	// Supplier Function Interface
	@Bean
	// Output Topic - supply-out-o
	Supplier<String> supply(){
		return ()-> "SUPPLIER";
	}


	// Consumer Function Interface
	@Bean
	// Input Topic - consumer-in-0
	Consumer<String> consume(){
		return v -> {
			System.out.print(v);
		};
	}

}
