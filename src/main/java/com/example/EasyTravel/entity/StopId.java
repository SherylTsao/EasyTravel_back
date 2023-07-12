package com.example.EasyTravel.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StopId implements Serializable {

	private String city;
	private String location;

	public StopId() {
		super();
	}

	public StopId(String city, String location) {
		super();
		this.city = city;
		this.location = location;
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
}
