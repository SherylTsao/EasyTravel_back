package com.example.EasyTravel.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.EasyTravel.constants.Abnormal;
import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Maintenance;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.MaintenanceDao;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.MaintenanceService;
import com.example.EasyTravel.vo.MaintenanceResponse;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	@Autowired
	private MaintenanceDao maintenanceDao;
	@Autowired
	private VehicleDao vehicleDao;

	/*
	 * 1.防呆:避免車牌輸入空產生空指針 2.要維修的車輛先在vehicle表內設定為false使其不能租借
	 * 3.note內容值間選取enum內代碼做選擇產生訊息 4.將車牌/開始維修時間/note插入到maintenance表內
	 * 5.若新增成功回傳成功message/反之6.藉由dao找Vehicle表格有沒有符合資格的車輛在改變狀態可以改變的話才能新增一張新的維修表單
	 */
	@Override
	public MaintenanceResponse AddAbnormal(String licensePlate) {
	    if (!StringUtils.hasText(licensePlate)) {
	        return new MaintenanceResponse(RtnCode.INCORRECT.getMessage());
	    }

	    // 將車牌號轉換為大寫
	    licensePlate = licensePlate.toUpperCase();

	    LocalDateTime startTime = LocalDateTime.now();
	    String status = "維護中"; // 將 "可租借" 改為 "維護中"
	    LocalDateTime latestCheckDate = null;

	    // 根據車牌號查詢車輛資訊
	    Vehicle vehicle = vehicleDao.getVehicleByLicensePlate(licensePlate);
	    if (vehicle == null) {
	        // 車輛不存在，返回錯誤響應
	        return new MaintenanceResponse(RtnCode.NOT_FOUND.getMessage());
	    }

	    if (vehicle.getStatus().equals("可租借")) {
	        // 如果車輛狀態為可租借，將其更新為維護中
	        vehicleDao.UpdateStatus(licensePlate, latestCheckDate, status);
	    } else {
	        // 車輛狀態已經為維護中，返回失敗響應
	        return new MaintenanceResponse(RtnCode.ALREADY_EXISTED.getMessage());
	    }

	    String note = Abnormal.E.getCode(); // 使用枚舉常量 E 的消息作為註釋內容

	    int rowsAffected = maintenanceDao.insertInfo(licensePlate, startTime, note);

	    return (rowsAffected > 0) ? new MaintenanceResponse(RtnCode.SUCCESS.getMessage())
	            : new MaintenanceResponse(RtnCode.NOT_FOUND.getMessage());
	}


	/*
	 * 1.防呆:避免車牌輸入空產生空指針,並且規定輸入價格要大於0符合邏輯 2.note內容值間選取enum內代碼做選擇產生訊息
	 * 3.用車牌尋找近5筆維修資料,如果end_time欄位為空，則進行完成單號的更新 4.更新內容為價格/完成時間/維修內容
	 * 5.並且在vehicle該維修車輛使用狀況改回true使其可以被租借 6.若更新成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse finishAbnormal(String licensePlate, Integer price, String note) {
	    if (!StringUtils.hasText(licensePlate) || price == null || price < 0) {
	        MaintenanceResponse response = new MaintenanceResponse();
	        response.setMessage(RtnCode.INCORRECT.getMessage());
	        return response;
	    }

	    LocalDateTime endTime = LocalDateTime.now();

	    String noteMessage = Abnormal.valueOf(note).getCode();
	    List<Maintenance> recentMaintenanceList = maintenanceDao.findRecentMaintenanceByLicensePlate(licensePlate);

	    for (Maintenance maintenance : recentMaintenanceList) {
	        if (maintenance.getEndTime() == null) {
	            int updatedRows = maintenanceDao.updateInfo(licensePlate, price, endTime, noteMessage);

	            // 更新車輛信息
	            String status = "可租借"; // 將 "維護中" 改為 "可租借"
	            LocalDateTime latestCheckDate = endTime; // 假設修理完成時間即為最新檢查日期
	            int updatedVehicleRows = vehicleDao.UpdateStatus(licensePlate, latestCheckDate, status);

	            return (updatedRows == 0 || updatedVehicleRows == 0)
	                    ? new MaintenanceResponse(RtnCode.NOT_FOUND.getMessage())
	                    : new MaintenanceResponse(RtnCode.SUCCESS.getMessage());
	        }
	    }

	    // 如果沒有找到需要更新的維修資料，則返回相應的響應
	    return new MaintenanceResponse(RtnCode.NOT_FOUND.getMessage());
	}


	/*
	 * 1.依照車牌跟開始維修時間找到該維修紀錄並且刪除 2.若刪除成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse deleteAbnormal(String licensePlate, LocalDateTime startTime) {
		int deletedRows = maintenanceDao.deleteInfo(licensePlate, startTime);
		return deletedRows > 0 ? new MaintenanceResponse(RtnCode.SUCCESS.getMessage())
				: new MaintenanceResponse(RtnCode.NOT_FOUND.getMessage());

	}

	/*
	 * 1.依照車牌找該車輛所有的維修紀錄並且回傳List 2.若查詢成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse searchByAbnormal(String licensePlate) {
		List<Maintenance> maintenanceList = maintenanceDao.searchInfoByLicensePlate(licensePlate);
		MaintenanceResponse response = new MaintenanceResponse();

		response.setMaintenanceList(maintenanceList);
		response.setMessage((maintenanceList != null && !maintenanceList.isEmpty()) ? RtnCode.SUCCESS.getMessage()
				: RtnCode.NOT_FOUND.getMessage());

		return response;
	}

	/*
	 * 1.依照開始時間跟結束時間尋找期間所有車輛的維修紀錄並且回傳List 2.若查詢成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse searchByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime) {
		List<Maintenance> maintenanceList = maintenanceDao.searchByStartTimeAndEndTimeInfo(startTime, endTime);
		MaintenanceResponse response = new MaintenanceResponse();

		response.setMaintenanceList(maintenanceList);
		response.setMessage((maintenanceList == null || maintenanceList.isEmpty()) ? RtnCode.NOT_FOUND.getMessage()
				: RtnCode.SUCCESS.getMessage());

		return response;
	}
	/*
	 * 1.依照note:E下尋找10筆車輛的未完成維修紀錄並且回傳List 2.若查詢成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse findLatestTenUnfinishedAbnormal() {
		List<Maintenance> maintenanceList = maintenanceDao.findAllByNote();
		MaintenanceResponse response = new MaintenanceResponse();
		response.setMaintenanceList(maintenanceList);
		response.setMessage((maintenanceList != null && !maintenanceList.isEmpty()) ? RtnCode.SUCCESS.getMessage()
				: RtnCode.NOT_FOUND.getMessage());

		return response;
	}

	/*
	 * 1.依照end_time不為空尋找所有已完成維修車輛的表單並且回傳List 2.若查詢成功回傳成功message/反之
	 */

	@Override
	public MaintenanceResponse findAllFinishedAbnormal() {
		List<Maintenance> maintenanceList = maintenanceDao.findAllFinishedCasesByEndTime();
		MaintenanceResponse response = new MaintenanceResponse();
		response.setMaintenanceList(maintenanceList);
		response.setMessage((maintenanceList != null && !maintenanceList.isEmpty()) ? RtnCode.SUCCESS.getMessage()
				: RtnCode.NOT_FOUND.getMessage());

		return response;
	}

}
