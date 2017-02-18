/**
 * @author Hasun Rathnayake
 * Date : 13/02/2017
 * Service configuration. Configured at service_monitor_context.xml
 */
package com.yukon.servicemonitor.config;

public class ServiceConfig {
	
	private int port; 				//Binding port
	private boolean runflag; 		//Run flag to advice service to keep running
	private String serviceName; 	//Name of the service
	private String host; 			//Binding host
	private long outageFrequency;	//Outage frequency
	private long outageDuration;	//Outage duration

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isRunflag() {
		return runflag;
	}

	public void setRunflag(boolean runflag) {
		this.runflag = runflag;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public long getOutageFrequency() {
		return outageFrequency;
	}

	public void setOutageFrequency(long outageFrequency) {
		this.outageFrequency = outageFrequency;
	}

	public long getOutageDuration() {
		return outageDuration;
	}

	public void setOutageDuration(long outageDuration) {
		this.outageDuration = outageDuration;
	}
	
}
