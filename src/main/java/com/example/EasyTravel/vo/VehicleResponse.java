package com.example.EasyTravel.vo;

import java.util.List;

import com.example.EasyTravel.entity.Vehicle;

public class VehicleResponse {
	
	private Vehicle vehicleEntity;
	
	private List<Vehicle> vehicleList;
	
	private String message;

	public VehicleResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VehicleResponse(String message) {
		super();
		this.message = message;
	}

	public VehicleResponse(Vehicle vehicleEntity, String message) {
		super();
		this.vehicleEntity = vehicleEntity;
		this.message = message;
	}

	public VehicleResponse(List<Vehicle> vehicleList, String message) {
		super();
		this.vehicleList = vehicleList;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Vehicle getVehicleEntity() {
		return vehicleEntity;
	}

	public void setVehicleEntity(Vehicle vehicleEntity) {
		this.vehicleEntity = vehicleEntity;
	}

	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}

	
}
