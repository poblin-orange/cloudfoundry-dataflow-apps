package com.orange.oss.cloudfoundry.dataflow;

import java.util.UUID;

import org.cloudfoundry.doppler.Envelope;
import org.cloudfoundry.doppler.FirehoseRequest;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.doppler.ReactorDopplerClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.reactor.uaa.ReactorUaaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Flux;

@Configuration
public class CloudFoundryConfig {

	@Bean
	DefaultConnectionContext connectionContext(FirehoseSourceProperties options) {
		
	    String apiHost=options.getHost();
		return DefaultConnectionContext.builder()
	        .apiHost(apiHost)
	        .skipSslValidation(options.isSkipSslValidation())
	        .build();
	}

	@Bean
	PasswordGrantTokenProvider tokenProvider(FirehoseSourceProperties options) {
	    return PasswordGrantTokenProvider.builder()
	        .password(options.getPassword())
	        .username(options.getUser())
	        .build();
	}
	
	@Bean
	ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
	    return ReactorCloudFoundryClient.builder()
	        .connectionContext(connectionContext)
	        .tokenProvider(tokenProvider)
	        .build();
	}

	@Bean
	ReactorDopplerClient dopplerClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
	    return ReactorDopplerClient.builder()
	        .connectionContext(connectionContext)
	        .tokenProvider(tokenProvider)
	        .build();
	}

	@Bean
	ReactorUaaClient uaaClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
	    return ReactorUaaClient.builder()
	        .connectionContext(connectionContext)
	        .tokenProvider(tokenProvider)
	        .build();
	}	
	
	@Bean
	 Flux<Envelope> firehoseFlux(ReactorDopplerClient dopplerClient){
		String subscriptionId = UUID.randomUUID().toString();
		FirehoseRequest request = FirehoseRequest.builder().subscriptionId(subscriptionId).build();
		Flux<Envelope> flux = dopplerClient.firehose(request);
		return flux;
	}

	
	
	
//	@Bean
//	DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
//	                                                     DopplerClient dopplerClient,
//	                                                     UaaClient uaaClient,
//	                                                     @Value("${cf.organization}") String organization,
//	                                                     @Value("${cf.space}") String space) {
//	    return DefaultCloudFoundryOperations.builder()
//	            .cloudFoundryClient(cloudFoundryClient)
//	            .dopplerClient(dopplerClient)
//	            .uaaClient(uaaClient)
//	            .organization(organization)
//	            .space(space)
//	            .build();
//	}	
}
