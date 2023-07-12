package com.example.EasyTravel.vo;

public class RentRequest {

	// 屬性
	private String account;
	private String licensePlate;
	private String city;
	private String location;
	private double odo;
	private int month;

	// getters & setters
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

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

	public double getOdo() {
		return odo;
	}

	public void setOdo(double odo) {
		this.odo = odo;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
}
