package com.example.EasyTravel.vo;

import java.time.LocalDate;

import com.example.EasyTravel.entity.UserInfo;

public class UserInfoResponse {

	UserInfo userInfo;

	private String account;

	private String name;

	private LocalDate birthday;

	private boolean motorcycleLicense;

	private boolean drivingLicense;

	private boolean vip;

	private LocalDate vipStartDate;

	private String message;

	public UserInfoResponse() {
		super();

	}

	public UserInfoResponse(String message) {
		super();
		this.message = message;
	}

	public UserInfoResponse(UserInfo userInfo, String message) {
		super();
		this.userInfo = userInfo;
		this.message = message;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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
