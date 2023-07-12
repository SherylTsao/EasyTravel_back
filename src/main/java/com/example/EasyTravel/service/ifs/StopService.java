package com.example.EasyTravel.service.ifs;

import java.util.List;

import com.example.EasyTravel.vo.StopResponse;

public interface StopService {

	// 新增站點
	public StopResponse addStop(String city, String location, int bikeAmount, int motorcycleAmount, int carAmount);

	// 顯示服務範圍
	public StopResponse showAllStops();
	
	// 尋找目標城市所有站點
	public StopResponse findCityStops(String city);
	
	// 尋找站點所有車輛
	public StopResponse findStopsVehicles(String city, String location);

	// 新增站點可借車輛
	public StopResponse renewAmount(String city, String location, int bikeAmount, int motorcycleAmount, int carAmount222);

	// 站點變更
	public StopResponse rentOrReturn(boolean isRent, String category, String city, String location);

	// 車輛調度
	public StopResponse dispatch(List<String> vehicleList, String city, String location);
}
