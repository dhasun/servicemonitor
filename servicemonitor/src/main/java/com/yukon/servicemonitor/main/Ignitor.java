/**
 * @author Hasun Rathnayake
 * Date 13/02/2017
 * Main class of the Service Monitor. Initiates the Spring context, dependency injection and starts related services.
 */
package com.yukon.servicemonitor.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yukon.servicemonitor.config.MonitorConfig;
import com.yukon.servicemonitor.monitor.Monitor;
import com.yukon.servicemonitor.service.Service;

public class Ignitor {
	
	private Ignitor(){} //Just to avoid making Object with new operator for this class
	private Ignitor(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
	}
	
	private static Ignitor IGNITER;														//Static instance of this
	private ApplicationContext applicationContext;										//Application context
	public static HashMap<String, Service> SERVICE_MAP;									//Service Map					
	public HashMap<String,Service> threadService 	= new HashMap<String,Service> ();	//Threaded Services map
	public static final String APPLICATION_CONTEXT 	= "service_monitor_context.xml";	//Spring application context name
	
	/**
	 * Returns an instance of this class
	 * @return an instance of Ignitor
	 */
	public static Ignitor getInstance(){
		return IGNITER ;
	}
	
	/**
	 * Returns the application context instance of this class
	 * @return an instance of application context
	 */
	public ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	/**
	 * Main method
	 */
	public static void main(String[] args) {		
		init();	
	}
	/**
	 * Initializes the application related stuffs
	 */
	private static void init(){
		try{
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT);
			IGNITER = new Ignitor(applicationContext);
			IGNITER.ignite();			
			Thread.sleep(10);
			
			//** Register callers hereafter
			IGNITER.registerForAService("A", 1000,2000); //Caller 1 registered for BETA
			IGNITER.registerForAService("GOOGLE", 100,2000);//Caller 2 registered for GOOGLE
			
		} catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	/**
	 * Ignites the services
	 */
	private void ignite(){
		MonitorConfig config = (MonitorConfig) getApplicationContext().getBean("mainConfig");		
		SERVICE_MAP = (HashMap<String, Service>) config.getServiceList();
		for (Map.Entry<String, Service> entry : SERVICE_MAP.entrySet()){
			threadService.put(entry.getKey(), entry.getValue());
		    entry.getValue().start();
		}
	}
	
	/**
	 * Register a caller with a specific service
	 * @param serviceName Name of the service to be registered
	 * @param pollingFrequency polling frequency of the service monitor
	 * @param graceTime Grace time to be waited if the Service is unavailable 
	 * @return A Monitor object of the relevant service
	 */
	public Monitor registerForAService(String serviceName,long pollingFrequency,long graceTime){				
		Monitor monitor = new Monitor();
		monitor.register(serviceName,pollingFrequency,graceTime);
		return monitor;
	}
	
	
}
