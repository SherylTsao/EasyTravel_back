package com.example.EasyTravel.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "maintenance")
public class Maintenance {

	@Id // Key:流水編號
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serial_number")
	private Integer serialNumber;
	// 車牌
	@Column(name = "license_plate")
	private String licensePlate;
	// 價格
	@Column(name = "price")
	private Integer price;
	// 維修的開始時間
	@Column(name = "start_time")
	private LocalDateTime startTime;
	// 維修的完成時間
	@Column(name = "end_time")
	private LocalDateTime endTime;
	// 車子的維修狀況
	@Column(name = "note")
	private String note;

	// 空的建構方法
	public Maintenance() {
		super();
		// TODO Auto-generated constructor stub
	}

	// 各屬性的建構方法
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	// 車牌跟維修開始時間的建構方法
	public Maintenance(String licensePlate, LocalDateTime startTime) {
		super();
		this.licensePlate = licensePlate;
		this.startTime = startTime;
	}

}
