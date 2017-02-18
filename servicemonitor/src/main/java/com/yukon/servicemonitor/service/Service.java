/**
 * @author Hasun Rathnayake
 * Date : 13/02/2017
 * Service class that connects to a specified port defined in a host
 */
package com.yukon.servicemonitor.service;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yukon.servicemonitor.config.ServiceConfig;

public class Service extends Thread{
	
	private ServiceConfig serviceConfig;	//Configuration of the service
	private Socket socket;					//Socket object
	private boolean serviceRunning;			//Running advice for the service
	private long lastOutageStartedDateStamp;//Last outage initiated time in milliseconds
	private long outageEndTimeDateStamp;	//Outage expire time in milliseconds
	private String message;					//Common message just to give a proper service status message

	public ServiceConfig getServiceConfig() {
		return serviceConfig;
	}

	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isServiceRunning() {
		return serviceRunning;
	}

	private void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}

	@Override
	public void run() {
		try{
			if(isOutageSet() && serviceConfig.isRunflag()){
				OutageTimer outageTimer = new OutageTimer(this);
				outageTimer.start();				
			}
			while(serviceConfig.isRunflag()){
				setStatus();
			}
		} catch(Exception  exception){
			exception.printStackTrace();
		}
	}
	
	/**
	 * Set online offline status of the service
	 */
	private void setStatus(){
		boolean isOnOutage = !isOutageSet() ? false : isOnOutage(System.currentTimeMillis());
		setServiceRunning(isOnOutage);
		message = isOnOutage ? "" : "SERVICE "+serviceConfig.getServiceName()+" IS "+( isConnected() ? "CONNECTED" : "NOT CONNECTED") +" , AND AVAILABLE";
	}
	
	/**
	 * Prints online status of the service if only when service is running
	 */
	public void getServiceStatus(){
		if(!serviceRunning)
			System.out.println(message);
	}
	
	/**
	 * Checks if the host is connected with the specified port
	 * @return true if and only if service is connected, else returns false.
	 */
	public boolean isConnected(){
		try{
			socket = new Socket(serviceConfig.getHost(), serviceConfig.getPort());
			return socket.isConnected();
		} catch(UnknownHostException exception) {
			return false;
		} catch (IOException e) {
			return false;
		} finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
	}
	
	/**
	 * Checks if an outage for a particular service is set
	 * @return true if and only if an outage frequency and an outage duration is set for the service
	 */
	private boolean isOutageSet(){
		return serviceConfig.getOutageFrequency() != 0 && serviceConfig.getOutageDuration() != 0 ;
	}
	
	/**
	 * Checks if the service is on an outage state
	 * @param currentSystemTime System time at a particular moment
	 * @return true if system time is passing on the outage time, else returns false
	 */
	public boolean isOnOutage(long currentSystemTime){
		return currentSystemTime >= lastOutageStartedDateStamp &&  currentSystemTime <= outageEndTimeDateStamp;
	}
	
	/**
	 * Updates outage started time and the outage end times for the next session
	 * @param outageRunTime System time at a moment where the outage fired. 
	 */
	protected void setOutageFrequency(long outageRunTime){
		//System.out.println(serviceConfig.getServiceName()+" IS GOING TO BE ON OFFLINE!");
		lastOutageStartedDateStamp = outageRunTime;
		outageEndTimeDateStamp = lastOutageStartedDateStamp + getServiceConfig().getOutageDuration();
	}
}

/**
 * @author Hasun Rathnayake
 * Outage monitor
 */
class OutageTimer extends Thread{
	
	Service service;	
	
	public OutageTimer(Service service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		try{
			long sleepTime = service.getServiceConfig().getOutageFrequency();
			while(true){
				sleep(sleepTime);
				service.setOutageFrequency(System.currentTimeMillis());
				sleepTime = service.getServiceConfig().getOutageFrequency() + service.getServiceConfig().getOutageDuration();
			}
		} catch(Exception exception){
			exception.printStackTrace();
		}
	}
}
