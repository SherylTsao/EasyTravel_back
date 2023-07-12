package com.example.EasyTravel.vo;

import java.util.List;

import com.example.EasyTravel.entity.Fee;

public class FeeResponse {

	// 屬性
	private String message;
	private List<Fee> feeList;
	private int total;

	/*
	 * 建構方法
	 * 1.()
	 * 2.(message)
	 * 3.(feeList, message)
	 * 4.(total, message)
	 */
	public FeeResponse() {
		super();
	}

	public FeeResponse(String message) {
		super();
		this.message = message;
	}

	public FeeResponse(List<Fee> feeList, String message) {
		super();
		this.feeList = feeList;
		this.message = message;
	}

	public FeeResponse(int total, String message) {
		super();
		this.total = total;
		this.message = message;
	}

	// getters & setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Fee> getFeeList() {
		return feeList;
	}

	public void setFeeList(List<Fee> feeList) {
		this.feeList = feeList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
