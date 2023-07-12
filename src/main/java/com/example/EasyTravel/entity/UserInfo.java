package com.example.EasyTravel.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_Info")
public class UserInfo {

	@Id
	@Column(name = "account")
	private String account;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "birthday")
	private LocalDate birthday;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "reg_time")
	private LocalDateTime regTime;
	
	@Column(name = "motorcycle_license")
	private boolean motorcycleLicense;
	
	@Column(name = "driving_license")
	private boolean drivingLicense;
	
	@Column(name = "vip")
	private boolean vip;
	
	@Column(name = "vip_start_date")
	private LocalDate vipStartDate;
	
	

	public UserInfo() {
		super();
		
	}

	public UserInfo(String account, String password, String name, LocalDate birthday, boolean active,
			LocalDateTime regTime, boolean motorcycleLicense, boolean drivingLicense, boolean vip,
			LocalDate vipStartDate) {
		super();
		this.account = account;
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		this.active = active;
		this.regTime = regTime;
		this.motorcycleLicense = motorcycleLicense;
		this.drivingLicense = drivingLicense;
		this.vip = vip;
		this.vipStartDate = vipStartDate;
	}


	public UserInfo(String account, String password, String name, LocalDate birthday, boolean active,
			LocalDateTime regTime) {
		super();
		this.account = account;
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		this.active = active;
		this.regTime = regTime;
	}

	public UserInfo(String account, boolean motorcycleLicense, boolean drivingLicense, boolean vip) {
		super();
		this.account = account;
		this.motorcycleLicense = motorcycleLicense;
		this.drivingLicense = drivingLicense;
		this.vip = vip;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getRegTime() {
		return regTime;
	}

	public void setRegTime(LocalDateTime regTime) {
		this.regTime = regTime;
	}

	public boolean isMotorcycleLicense() {
		return motorcycleLicense;
	}

	public void setMotorcycleLicense(boolean motorcycleLicense) {
		this.motorcycleLicense = motorcycleLicense;
	}

	public boolean isDrivingLicense() {
		return drivingLicense;
	}

	public void setDrivingLicense(boolean drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public LocalDate getVipStartDate() {
		return vipStartDate;
	}

	public void setVipStartDate(LocalDate vipStartDate) {
		this.vipStartDate = vipStartDate;
	}
	
	
	
	
}
