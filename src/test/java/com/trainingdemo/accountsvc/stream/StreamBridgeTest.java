package com.trainingdemo.accountsvc.stream;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainingdemo.accountsvc.dto.TransactionNotificationDto;
import org.junit.jupiter.api.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamBridgeTest {

	@Test
	public void shouldSendMessageUsingStreamBridge() throws Exception {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
				TestChannelBinderConfiguration.getCompleteConfiguration(
						EmptyConfiguration.class)).web(WebApplicationType.NONE).run(
				"--spring.cloud.stream.source=transaction-notification",
				"--spring.cloud.stream.bindings.transaction-notification-out-0.destination=transaction-notification",
				"--spring.datasource.url=jdbc:h2:mem:testdb123",
				"--spring.datasource.driver-class-name=org.h2.Driver",
				"--spring.datasource.username=sa",
				"--spring.datasource.password=",
				"--management.metrics.export.wavefront.enabled=false")) {
			StreamBridge streamBridge = context.getBean(StreamBridge.class);
			TransactionNotificationDto transactionNotificationDto = new TransactionNotificationDto(123L);
			streamBridge.send("transaction-notification-out-0", MessageBuilder.withPayload(transactionNotificationDto).build());

			OutputDestination output = context.getBean(OutputDestination.class);
			Message<byte[]> message = output.receive(1000, "transaction-notification");

			// Convert byte[] into a Map<String,Object> using Jackson TypeReference
			Map<String, Object> value = new ObjectMapper().readValue(message.getPayload(), new TypeReference<>() {});
			assertThat(value.get("transactionId")).isEqualTo(123);

			// Convert byte[] into a Java object
			TransactionNotificationDto notificationDto = new ObjectMapper().readValue(message.getPayload(), TransactionNotificationDto.class);
			assertThat(notificationDto.getTransactionId()).isEqualTo(123);

		}
	}

	@EnableAutoConfiguration
	public static class EmptyConfiguration {

	}
}
