package com.trainingdemo.accountsvc.controller;

import com.trainingdemo.accountsvc.controller.FunctionDemo.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(OutputCaptureExtension.class)
class FunctionDemoTest {

	FunctionDemo cut = new FunctionDemo();

	@Test
	void shouldReturnUpperCaseValue() {

		Person person = new Person();
		person.setName("Marc");

		StreamBridge streamBridge = Mockito.mock(StreamBridge.class);
		Mockito.when(streamBridge.send(any(), any())).thenReturn(true);



		Message<Person> message = MessageBuilder.withPayload(person).build();

		String output = cut.process(streamBridge).apply(message);
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