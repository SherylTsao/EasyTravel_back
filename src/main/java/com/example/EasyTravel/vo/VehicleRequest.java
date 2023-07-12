package com.example.EasyTravel.vo;

import java.time.LocalDate;

public class VehicleRequest {

//	車牌
	private String licensePlate;

//	分類
	private String category;

//	cc
	private int cc;

//	起始服役日
	private LocalDate startServingDate;

//	最新檢查日
	private LocalDate latestCheckDate;

//	可租借狀態
	private String status;

//	所在城市
	private String city;

//	所在站點
	private String location;

//	總里程
	private double odo;

//	價格
	private int price;

//	建構方法 -------------------------
	public VehicleRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VehicleRequest(String licensePlate, String category, int cc, LocalDate startServingDate,
			LocalDate latestCheckDate, String status, String city, String location, double odo, int price) {
		super();
		this.licensePlate = licensePlate;
		this.category = category;
		this.cc = cc;
		this.startServingDate = startServingDate;
		this.latestCheckDate = latestCheckDate;
		this.status = status;
		this.city = city;
		this.location = location;
		this.odo = odo;
		this.price = price;
	}

//  addCar用
	public VehicleRequest(String licensePlate, String category, int cc, int price) {
		super();
		this.licensePlate = licensePlate;
		this.category = category;
		this.cc = cc;
		this.price = price;
	}

//  updateCarInfo用
	public VehicleRequest(String licensePlate, String status, double odo) {
		super();
		this.licensePlate = licensePlate;
		this.status = status;
		this.odo = odo;
	}

//	scrapCar用
	public VehicleRequest(String licensePlate) {
		super();
		this.licensePlate = licensePlate;
	}

//	findCarByCategory用
	public void categoryVehicleRequest(String category) {
		this.category = category;
	}

//	get / set -------------------------
	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCc() {
		return cc;
	}

	public void setCc(int cc) {
		this.cc = cc;
	}

	public LocalDate getStartServingDate() {
		return startServingDate;
	}

	public void setStartServingDate(LocalDate startServingDate) {
		this.startServingDate = startServingDate;
	}

	public LocalDate getLatestCheckDate() {
		return latestCheckDate;
	}

	public void setLatestCheckDate(LocalDate latestCheckDate) {
		this.latestCheckDate = latestCheckDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
