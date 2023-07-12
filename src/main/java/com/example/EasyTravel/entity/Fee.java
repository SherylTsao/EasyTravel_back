package com.example.EasyTravel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fee")
public class Fee {
	
	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serial_number")
	private Integer serialNumber;
	
	// 方案名稱
	@Column(name = "project")
	private String project;
	
	// 排氣量
	@Column(name = "cc")
	private int cc;
	
	// 費率
	@Column(name = "rate")
	private double rate;
	
	// 時間閾值
	@Column(name = "threshold")
	private int threshold;

	/*
	 * 建構方法
	 * 1.()
	 * 2.(project, cc, rate, threshold)
	 */
	public Fee() {
		super();
	}

	public Fee(String project, int cc, double rate, int threshold) {
		super();
		this.project = project;
		this.cc = cc;
		this.rate = rate;
		this.threshold = threshold;
	}

	// getters & setters
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getCc() {
		return cc;
	}

	public void setCc(int cc) {
		this.cc = cc;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

}
