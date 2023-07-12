package com.example.EasyTravel.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StopRequest {

	// 屬性
	private String city;
	private String location;
	@JsonProperty("bike_amount")
	private int bikeAmount;
	@JsonProperty("motorcycle_amount")
	private int motorcycleAmount;
	@JsonProperty("car_amount")
	private int carAmount;
	private boolean isRent;
	private String category;
	@JsonProperty("vehicle_list")
	private List<String> vehicleList;

	// getters & setters
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getBikeAmount() {
		return bikeAmount;
	}

	public void setBikeAmount(int bikeAmount) {
		this.bikeAmount = bikeAmount;
	}

	public int getMotorcycleAmount() {
		return motorcycleAmount;
	}

	public void setMotorcycleAmount(int motorcycleAmount) {
		this.motorcycleAmount = motorcycleAmount;
	}

	public int getCarAmount() {
		return carAmount;
	}

	public void setCarAmount(int carAmount) {
		this.carAmount = carAmount;
	}

	public boolean isRent() {
		return isRent;
	}

	public void setRent(boolean isRent) {
		this.isRent = isRent;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getVehicleList() {
		return vehicleList;
	}

	public void setVehicleList(List<String> vehicleList) {
		this.vehicleList = vehicleList;
	}

}
