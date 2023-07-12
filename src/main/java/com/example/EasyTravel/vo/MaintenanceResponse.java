package com.example.EasyTravel.vo;

import java.util.List;

import com.example.EasyTravel.entity.Maintenance;



public class MaintenanceResponse {
	
	//屬性
	
	private String message;
	private List<Maintenance>maintenanceList;
	private String note;
	
	//空參數的建構方法
	public MaintenanceResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//message的建構方法
	public MaintenanceResponse(String message) {
		super();
		this.message = message;
	}
	
	//get.set方法
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Maintenance> getMaintenanceList() {
		return maintenanceList;
	}
	public void setMaintenanceList(List<Maintenance> maintenanceList) {
		this.maintenanceList = maintenanceList;
	}
	

}
