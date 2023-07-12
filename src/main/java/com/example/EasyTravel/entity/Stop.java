package com.example.EasyTravel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "stop")
@IdClass(value = StopId.class)
public class Stop {
	
	// 城市
	@Id
	@Column(name = "city")
	private String city;
	
	// 站點
	@Id
	@Column(name = "location")
	private String location;
	
	// 腳踏車存量
	@Column(name = "bike_amount")
	private int bikeAmount;
	
	// 機車存量
	@Column(name = "motorcycle_amount")
	private int motorcycleAmount;
	
	// 汽車存量
	@Column(name = "car_amount")
	private int carAmount;

	/*
	 * 建構方法
	 * 1.()
	 * 2.(city, location, bikeAmount, motorcycleAmount, carAmount)
	 */
	public Stop() {
		super();
	}

	public Stop(String city, String location, int bikeAmount, int motorcycleAmount, int carAmount) {
		super();
		this.city = city;
		this.location = location;
		this.bikeAmount = bikeAmount;
		this.motorcycleAmount = motorcycleAmount;
		this.carAmount = carAmount;
	}

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

}
