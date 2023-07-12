package com.example.EasyTravel.vo;

import java.time.LocalDate;

public class UserInfoRequest {

	private String account;
	
	private String password;
	
	private String name;
	
	private LocalDate birthday;
	
	private boolean motorcycleLicense;
	
	private boolean drivingLicense;
	
	private boolean vip;
	
	private LocalDate vipStartDate;
	
	private String message;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
