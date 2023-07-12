package com.example.EasyTravel.service.ifs;

import java.time.LocalDateTime;

import com.example.EasyTravel.vo.MaintenanceResponse;

public interface MaintenanceService {

	// 新增單號
	public MaintenanceResponse AddAbnormal(String licensePlate);

	// 完成單號
	public MaintenanceResponse finishAbnormal(String licensePlate,Integer price, String note);

	// 註銷單號
	public MaintenanceResponse deleteAbnormal(String licensePlate, LocalDateTime startTime);

	// 查詢單號
	public MaintenanceResponse searchByAbnormal(String licensePlate);

	// 藉由保養開始日到結束日查詢期間所有單號
	public MaintenanceResponse searchByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);
	
	//尋找未完成表單的前10筆	
	public MaintenanceResponse findLatestTenUnfinishedAbnormal();
	
	//尋找全部已完成表單
	public MaintenanceResponse findAllFinishedAbnormal();

	

}
