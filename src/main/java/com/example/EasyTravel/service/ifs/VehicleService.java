package com.example.EasyTravel.service.ifs;

import com.example.EasyTravel.vo.VehicleResponse;

public interface VehicleService {

//	新增車輛
	public VehicleResponse addCar(String licensePlate, String category, int cc, int price);
	
//	修改資訊
	public VehicleResponse updateCarInfo(String licensePlate, double odo, String status);
	
//	報廢車輛
	public VehicleResponse scrapCar(String licensePlate);
	
//	車種找車輛
	public VehicleResponse findCarByCategory(String category);
	
//	找所有資訊
	public VehicleResponse findAllCar();

	//接近報廢車輛搜尋
	public VehicleResponse findCarNearScrap();
	
	//年度未檢修車輛搜尋
	public VehicleResponse findCarNeedCheck();
	
}
