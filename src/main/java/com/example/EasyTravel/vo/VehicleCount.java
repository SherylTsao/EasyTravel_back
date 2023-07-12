package com.example.EasyTravel.vo;

public class VehicleCount {

	private String category;
	private long count;

	public VehicleCount(String category, long count) {
		super();
		this.category = category;
		this.count = count;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}
