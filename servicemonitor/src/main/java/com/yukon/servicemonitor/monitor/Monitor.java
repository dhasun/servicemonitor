/**
 * @author Hasun Rathnayake
 * Date : 18/02/2017
 * Monitoring class for a particular service
 */
package com.yukon.servicemonitor.monitor;

import java.util.HashMap;

import com.yukon.servicemonitor.config.MonitorConfig;
import com.yukon.servicemonitor.main.Ignitor;
import com.yukon.servicemonitor.service.Service;

public class Monitor extends Thread{
	
	private Service service;		//Service to be monitored
	private boolean running = true; //Running advice flag
	private long pollingFrequency;	//Polling frequency
	private long graceTime;			//Grace time
	private String serviceName;		//Name of the service
	
	public Service getService() {
		return service;
	}
	
	
	@Override
	public void run() {
		try{
			System.out.println(serviceName+" IS "+ ( service.isConnected() ? "UP" : "DOWN"));
			while(running){
				//The monitor should detect multiple callers registering interest in the same service, and should not poll any service more frequently than once a second.
				int runningServices = MonitorConfig.runningServices(serviceName);
				long frequency 		= runningServices > 1 ? 1000 : pollingFrequency;
				sleep(frequency);
				if(!service.isServiceRunning())
					sleep(graceTime);
				
				service.getServiceStatus();				
			}
		} catch(Exception exception){
			exception.printStackTrace();
			evac();
		}
	}
	
	/**
	 * Register a particular caller with a service.
	 * @param serviceName Requested service name
	 * @param pollingFrequency Frequency of the polling
	 * @param graceTime Grace time to be wait if the service is not online
	 */
	public void register(String serviceName,long pollingFrequency,long graceTime){
		Ignitor ignitor = Ignitor.getInstance();
		HashMap<String,Service> threadService = ignitor.threadService;
		MonitorConfig.addToRunning(serviceName);
		this.serviceName 		= serviceName;
		this.service 			= threadService.get(serviceName);
		this.pollingFrequency 	= pollingFrequency;
		this.graceTime 			= graceTime;
		if(service != null)
			this.start();
		else
			System.out.println(serviceName+" IS NOT IDENTIFIED AS A VALID SERVICE!");
	}
	
	/**
	 * Evacuating monitor off a particular service.
	 */
	public void evac(){
		System.out.println("Evacuating monitor off "+serviceName);
		MonitorConfig.removeFromRunning(serviceName);
		this.running = false;
	}
}
