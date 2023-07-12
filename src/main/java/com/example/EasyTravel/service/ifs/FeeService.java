package com.example.EasyTravel.service.ifs;

import java.time.Duration;

import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.vo.FeeResponse;

public interface FeeService {

	// 新增費率資料
	public FeeResponse addProject(String project, int cc, double rate, int threshold);

	// 刪除費率資料
	public FeeResponse deleteProject(String project, Integer cc, Double rate);

	// 尋找交通工具費率
	public FeeResponse findProjects(String project,Integer cc);

	// 計算費率
	public FeeResponse calculate(Vehicle vehicle, boolean isVip, Duration period);
	
	// 顯示全部
	public FeeResponse showAllFees();
}
