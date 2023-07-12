package com.example.EasyTravel.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "finance")
public class Finance {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serial_number")
	private Integer serialNumber;

	// 主項目
	@Column(name = "title")
	private String title;

	// 次項目
	@Column(name = "detail")
	private String detail;

	// 金額
	@Column(name = "price")
	private int price;

	// 建立日期
	@Column(name = "build_time")
	private LocalDateTime buildTime = LocalDateTime.now();

	/*
	 * 建構方法
	 * 1.()
	 * 2.(title, detail, price, buildTime)
	 */
	public Finance() {
		super();
	}

	public Finance(String title, String detail, int price, LocalDateTime buildTime) {
		super();
		this.title = title;
		this.detail = detail;
		this.price = price;
		this.buildTime = buildTime;
	}

	// getters & setters
	public Integer getSerialNumber() {
		return serialNumber;
	}
	
	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public LocalDateTime getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(LocalDateTime buildTime) {
		this.buildTime = buildTime;
	}

}
