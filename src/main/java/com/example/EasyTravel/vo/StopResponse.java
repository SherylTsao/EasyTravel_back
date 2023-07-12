package com.example.EasyTravel.vo;

import java.util.List;

import com.example.EasyTravel.entity.Stop;
import com.example.EasyTravel.entity.Vehicle;

public class StopResponse {

	// 屬性
	private String message;
	private Stop stop;
	private List<Stop> stopList;
	private List<Vehicle> vehicleList;

	/*
	 * 建構方法
	 * 1.()
	 * 2.(message)
	 * 3.(stop, message)
	 * 4.(stopList, message)
	 * 5.(stop, vehicleList, message)
	 */
	public StopResponse() {
		super();
	}

	public StopResponse(String message) {
		super();
		this.message = message;
	}

	public StopResponse(Stop stop, String message) {
		super();
		this.stop = stop;
		this.message = message;
	}

	public StopResponse(List<Stop> stopList, String message) {
		super();
		this.stopList = stopList;
		this.message = message;
	}
	
	public StopResponse(Stop stop, List<Vehicle> vehicleList, String message) {
		super();
		this.stop = stop;
		this.vehicleList = vehicleList;
		this.message = message;
	}

	// getters & setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public List<Stop> getStopList() {
		return stopList;
	}

	public void setStopList(List<Stop> stopList) {
		this.stopList = stopList;
	}

	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

}
