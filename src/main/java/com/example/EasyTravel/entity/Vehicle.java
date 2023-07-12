package com.example.EasyTravel.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle {

//	PK車牌號碼
	@Id
	@Column(name = "license_plate")
	private String licensePlate;
	
//	分類
	@Column(name = "category")
	private String category;
	
//	cc
	@Column(name = "cc")
	private int cc;
	
//	起始服役, 初始為建立當日
	@Column(name = "start_serving_date")
	private LocalDate startServingDate = LocalDate.now();
	
//	最新檢查日, 初始為建立當日
	@Column(name = "latest_check_date")
	private LocalDate latestCheckDate = LocalDate.now();
	
//	可租借狀態, 初始為false
	@Column(name = "status")
	private String status = "未啟用";
	
//	所在城市
	@Column(name = "city")
	private String city;
	
//	所在站點
	@Column(name = "location")
	private String location;
	
//	總里程, 初始為0
	@Column(name = "odo")
	private double odo = 0;
	
//	價格
	@Column(name = "price")
	private int price;

	
//	建構方法 -------------------------
	public Vehicle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Vehicle(String licensePlate, String category, int cc, LocalDate startServingDate,
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

	
//	addCar
	public Vehicle(String licensePlate, String category, int cc, int price) {
		this.licensePlate = licensePlate;
		this.category = category;
		this.cc = cc;
		this.price = price;
	}
	
	public void updateVehicleEntity(String status, double odo) {
		this.status = status;
		this.odo = odo;
	}
	
	// for rent
	public Vehicle(String licensePlate, String category) {
		super();
		this.licensePlate = licensePlate;
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
