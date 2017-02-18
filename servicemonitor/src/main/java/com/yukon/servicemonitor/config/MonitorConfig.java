/**
 * @author Hasun Rathnayake
 * Date : 17/02/2017
 * Main configuration
 */
package com.yukon.servicemonitor.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.yukon.servicemonitor.service.Service;

public class MonitorConfig {
	
	private Map<String,Service> serviceList;	//Service list is configured at the service_monitor_context.xml
	
	//Currently engaged services are stored here. Volatile and static is for thread safety
	private volatile static List<String> RUNNING_SERVICES = new ArrayList<String>();

	public Map<String, Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(Map<String, Service> serviceList) {
		this.serviceList = serviceList;
	}

	public static void addToRunning(String service){
		RUNNING_SERVICES.add(service);
	}
	
	public static void removeFromRunning(String service){
		int index = RUNNING_SERVICES.indexOf(service);
		RUNNING_SERVICES.remove(index);
	}
	
	public static int runningServices(String service){
		return Collections.frequency(RUNNING_SERVICES, service);
	}
	
	public static List<String> getRunningServices(){
		return RUNNING_SERVICES;
	}
	
}
