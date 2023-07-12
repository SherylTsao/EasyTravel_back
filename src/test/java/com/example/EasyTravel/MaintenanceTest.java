package com.example.EasyTravel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.Abnormal;
import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Maintenance;
import com.example.EasyTravel.repository.MaintenanceDao;
import com.example.EasyTravel.service.ifs.MaintenanceService;
import com.example.EasyTravel.vo.MaintenanceResponse;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MaintenanceTest {

	@Autowired
	private MaintenanceDao mDao;

	@Autowired
	private MaintenanceService mSer;

	@BeforeEach
	private void BeforEach() {
		List<Maintenance> list = new ArrayList<>();
		Maintenance m1 = new Maintenance("115", LocalDateTime.of(2022, 12, 8, 12, 1));
		list.add(m1);
		mDao.saveAll(list);

	}

	@AfterAll
	private void AfterAll() {
		mDao.deleteInfo("115", LocalDateTime.of(2022, 12, 8, 12, 1));

	}

	@Test
	// Dao方法新增測試
	void insertInfoTest() {
		int res = mDao.insertInfo("AZ-12345", LocalDateTime.of(2022, 12, 8, 12, 1), Abnormal.E.getMessage());
		Assert.isTrue(res == 1, RtnCode.TEST1_ERROR.getMessage());

	}

	@Test
	// Dao方法更新測試
	void updateInfoTest() {
		int res = mDao.updateInfo("AZ-12345", 1000, LocalDateTime.of(2022, 12, 10, 13, 6), Abnormal.A01.getMessage());
		Assert.isTrue(res == 1, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	// Dao方法查詢測試
	void searchInfoTest() {
		List<Maintenance> res = mDao.searchInfoByLicensePlate("115");
		Assert.isTrue(res.size() > 0, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	// Dao方法用時間查詢測試
	void searchByStartTimeAndEndTimeInfoTest3() {
		LocalDateTime startTime = LocalDateTime.of(2022, 12, 8, 0, 0, 0);
		LocalDateTime endTime = LocalDateTime.of(2023, 6, 5, 23, 59, 59);

		List<Maintenance> result = mDao.searchByStartTimeAndEndTimeInfo(startTime, endTime);

		Assert.isTrue(result.size() == 6, RtnCode.TEST1_ERROR.getMessage());

	}

	@Test
	// Dao方法用車牌查詢該車輛近5次維修紀錄測試
	void findRecentMaintenanceByLicensePlateTEST() {
		String licensePlate = "555AZ-1";
		List<Maintenance> result = mDao.findRecentMaintenanceByLicensePlate(licensePlate);

		Assert.isTrue(result.size() == 1, RtnCode.TEST1_ERROR.getMessage());

	}

//	@Test
//	// Dao方法直接找出前10筆未完成的單號
//	void FindLatestTenByNoteTest() {
//		List<Maintenance> result = mDao.findLatestTenByNote();
//
//		Assert.isTrue(result.size() == 1, RtnCode.TEST1_ERROR.getMessage());
//
//	}

	@Test
	// Dao方法直接找出所有已完成的單號
	void findAllFinishedCasesByPrice() {
		List<Maintenance> result = mDao.findAllFinishedCasesByEndTime();

		Assert.isTrue(result.size() == 2, RtnCode.TEST1_ERROR.getMessage());

	}

	@Test
	// service完成新增單號方法測試
	void addAbnormalTest() {
		MaintenanceResponse response = mSer.AddAbnormal("AZ-1234");
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"AddAbnormal test failed: " + response.getMessage());
	}

	@Test
	// service完成維修單號方法測試
	void finishAbnormalTest() {
		MaintenanceResponse response = mSer.finishAbnormal("AZ-1234", 70000, Abnormal.A01.getCode());
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"finishAbnormal test failed: " + response.getMessage());
	}

	@Test
	// service刪除維修單號方法測試
	void deleteAbnormalTest() {

		MaintenanceResponse response = mSer.deleteAbnormal("555AZ-1", LocalDateTime.of(2023, 6, 5, 0, 0, 0));
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"deleteAbnormal test failed: " + response.getMessage());
	}

	@Test
	// service以車牌查詢維修單號方法測試
	void searchByAbnormalTest() {
		MaintenanceResponse response = mSer.searchByAbnormal("555AZ-1");
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"searchByAbnormal test failed: " + response.getMessage());
	}

	@Test
	// service以期間查詢維修單號方法測試
	void searchByStartTimeAndEndTimeTest() {
		LocalDateTime startTime = LocalDateTime.of(2022, 12, 8, 12, 1);
		LocalDateTime endTime = LocalDateTime.of(2023, 6, 6, 0, 0, 7);
		MaintenanceResponse response = mSer.searchByStartTimeAndEndTime(startTime, endTime);
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"searchByStartTimeAndEndTime test failed: " + response.getMessage());
	}

	@Test
	// service以查詢最新10筆未完成單號方法測試
	void findLatestTenUnfinishedAbnormalTest() {
		MaintenanceResponse response = mSer.findLatestTenUnfinishedAbnormal();
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"searchByStartTimeAndEndTime test failed: " + response.getMessage());
	}

	@Test
	// service以查詢所有已完成單號方法測試
	void findAllFinishedAbnormalTest() {
		MaintenanceResponse response = mSer.findAllFinishedAbnormal();
		Assert.isTrue(response.getMessage().equals(RtnCode.SUCCESS.getMessage()),
				"searchByPrice test failed: " + response.getMessage());
	}

}
