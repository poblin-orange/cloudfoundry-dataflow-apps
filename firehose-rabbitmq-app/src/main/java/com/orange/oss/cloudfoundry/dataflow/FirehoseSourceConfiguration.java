package com.orange.oss.cloudfoundry.dataflow;

import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.doppler.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import reactor.core.publisher.Flux;

@EnableBinding(Source.class)
@EnableConfigurationProperties({ FirehoseSourceProperties.class })

@EnableScheduling

public class FirehoseSourceConfiguration {

	private static Logger logger = LoggerFactory.getLogger(FirehoseSourceConfiguration.class.getName());

	public static class pollTask {

		@Autowired
		Flux<Envelope> dopplerFlux;

		@Autowired
		MessageChannel output;

		@Scheduled(fixedRate = 500)
		public void poll() {

			// headers.put(APPLICATION_ID.asHeader(),
			// applicationLog.getAppId());
			// headers.put(SOURCE_ID.asHeader(), applicationLog.getSourceId());
			// headers.put(MESSAGE_TYPE.asHeader(),
			// applicationLog.getMessageType());
			// headers.put(SOURCE_NAME.asHeader(),
			// applicationLog.getSourceName());
			// headers.put(TIMESTAMP.asHeader(), applicationLog.getTimestamp());
			Envelope e = dopplerFlux.blockFirst();
			Map<String, Object> headers = new HashMap<>();
			headers.put("deployment", e.getDeployment());
			headers.put("eventType", e.getEventType());
			headers.put("index", e.getIndex());
			headers.put("job", e.getJob());
			headers.put("origin", e.getOrigin());
			headers.put("timestamp", e.getTimestamp());
			output.send(MessageBuilder.withPayload(e.getLogMessage()).copyHeaders(headers).build());

		}

	}

}
