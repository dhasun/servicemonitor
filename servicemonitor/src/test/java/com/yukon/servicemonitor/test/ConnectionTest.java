package com.yukon.servicemonitor.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.junit.Test;

import com.yukon.servicemonitor.config.ServiceConfig;
import com.yukon.servicemonitor.main.Ignitor;
import com.yukon.servicemonitor.monitor.Monitor;
import com.yukon.servicemonitor.service.Service;

public class ConnectionTest extends TestCase{

	@Test
	public void testRegisterForAService(){
		try{
			Monitor monitor = Ignitor.getInstance().registerForAService("GOOGLE", 1000, 2000);
			
			//Check if the Monitor is not null
			assertNotNull(monitor);
			
			//Check if the returned object is an instance of a Monitor
			assertEquals(true, monitor instanceof Monitor);
			
			//Test if the Monitor is valid and alive
			assertEquals(true, monitor.isAlive());			
			
			//Check if the Service of the Monitor is conected
			assertEquals(true, monitor.getService().isConnected());
			
		} catch(Exception exception){
			fail(exception.getLocalizedMessage());
		}
	}
	
	@Test
	public void testInit(){
		try{
			Method method = Ignitor.class.getDeclaredMethod("init", null);
			method.setAccessible(true);
			method.invoke(Ignitor.getInstance(), null);
			
			//Check if the Ignitor instance is not null
			assertNotNull(Ignitor.getInstance());
			
		} catch(Exception exception){
			fail(exception.getLocalizedMessage());
		}
	}
	
	@Test
	public void testIgnite(){		
		try{	
			//Check if the service map is not null
			assertNotNull(Ignitor.SERVICE_MAP);
			
			//Check if the service map is not empty
			assertTrue(!Ignitor.SERVICE_MAP.isEmpty());
			
			//Check if the Thread service map is not null
			assertNotNull(Ignitor.getInstance().threadService);
			
			//Check if the Thread service map is not empty
			assertTrue(!Ignitor.getInstance().threadService.isEmpty());
			
		} catch(Exception exception){
			fail(exception.getLocalizedMessage());
		}
	}
	
	@Test
	public void testServiceConnection(){
		
		//Good connection
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.setHost("www.google.com");
		serviceConfig.setOutageDuration(1000);
		serviceConfig.setOutageFrequency(3000);
		serviceConfig.setPort(80);
		serviceConfig.setServiceName("GOOGLE");
		
		Service service = new Service();
		service.setServiceConfig(serviceConfig);
		service.start();
		
		//Test if the service is connected
		assertEquals(true, service.isConnected());
		
		//Test if the service is on an outage
		//assertEquals(true, service.isOnOutage(System.currentTimeMillis()));
		
	}
	
	@Test
	public void testApplicationContextName(){
		assertEquals("service_monitor_context.xml", Ignitor.APPLICATION_CONTEXT);
	}
	
}
