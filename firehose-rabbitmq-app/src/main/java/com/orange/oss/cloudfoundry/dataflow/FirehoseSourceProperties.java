package com.orange.oss.cloudfoundry.dataflow;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("cloudfoundry")
public class FirehoseSourceProperties {

	private String host;
	private String user;
	private String password;
	private boolean skipSslValidation=false;

	
	public boolean isSkipSslValidation() {
		return skipSslValidation;
	}
	public void setSkipSslValidation(boolean skipSslValidation) {
		this.skipSslValidation = skipSslValidation;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
