package com.orange.oss.cloudfoundry.dataflow;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import reactor.core.publisher.Flux;

@EnableBinding(Source.class)
public class FirehoseSource extends MessageProducerSupport  {
	
	private static Logger logger=LoggerFactory.getLogger(FirehoseSource.class.getName());	
	@Autowired
	ReactorDopplerClient doppler;
	
	protected FirehoseSource(MessageChannel out){
		
	}

	public enum LoggregatorHeaders {

		APPLICATION_ID("loggr_applicationId"),
		MESSAGE_TYPE("loggr_messageType"),
		SOURCE_ID("loggr_sourceId"),
		SOURCE_NAME("loggr_sourceName"),
		TIMESTAMP("loggr_timestamp");

		private final String key;

		private LoggregatorHeaders(String key) {
			this.key = key;
		}

		public String asHeader() {
			return key;
		}

	}

	
	//starting source
	@Override
	protected void doStart() {
		
		String subscriptionId=UUID.randomUUID().toString();
		FirehoseRequest request=FirehoseRequest.builder().subscriptionId(subscriptionId).build();
		Flux<Envelope> flux=this.doppler.firehose(request);
		//flux.map();
		
	}

	

	

	private void sendMessage(Envelope applicationLog) {
		Map<String, Object> headers = new HashMap<>();
		
//		headers.put(APPLICATION_ID.asHeader(), applicationLog.getAppId());
//		headers.put(SOURCE_ID.asHeader(), applicationLog.getSourceId());
//		headers.put(MESSAGE_TYPE.asHeader(), applicationLog.getMessageType());
//		headers.put(SOURCE_NAME.asHeader(), applicationLog.getSourceName());
//		headers.put(TIMESTAMP.asHeader(), applicationLog.getTimestamp());
		
		sendMessage(MessageBuilder.withPayload(applicationLog.getLogMessage())
				.copyHeaders(headers)
				.build());
	}




	
	
	
	
	
	

}
