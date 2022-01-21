package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.controller.FunctionDemo.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
class FunctionDemoTest {

	FunctionDemo cut = new FunctionDemo();

	@Test
	@Disabled
	void shouldReturnUpperCaseValue() {

		Person person = new Person();
		person.setName("Marc");

		Message<Person> message = MessageBuilder.withPayload(person).build();

		String output = cut.process(null).apply(message);
		Assertions.assertThat(output).isEqualTo("MARC");

	}

	@Test
	void shouldReturnSupplierForSupply() {

		String output = cut.supply().get();
		Assertions.assertThat(output).isEqualTo("SUPPLIER");
	}

	@Test
	void shouldConsumeMessage(CapturedOutput capturedOutput) {

		cut.consume().accept("Spring");
		Assertions.assertThat(capturedOutput.getOut()).isEqualTo("Spring");

	}
}