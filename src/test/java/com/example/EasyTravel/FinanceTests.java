package com.example.EasyTravel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Finance;
import com.example.EasyTravel.repository.FinanceDao;
import com.example.EasyTravel.service.ifs.FinanceService;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FinanceTests {

	@Autowired
	private FinanceDao fDao;

	@Autowired
	private FinanceService fSer;

	@BeforeAll
	private void BeforeAll() {
		// 建立假資料
		fDao.saveAll(
				new ArrayList<>(Arrays.asList(new Finance("test1", "d", 10, LocalDateTime.of(2023, 5, 20, 0, 0, 0)),
						new Finance("test1", "c", 10, LocalDateTime.of(2023, 5, 20, 0, 0, 0)),
						new Finance("test2", "d", 10, LocalDateTime.of(2023, 5, 20, 0, 0, 0)),
						new Finance("test2", "d", 10, LocalDateTime.of(2023, 6, 20, 0, 0, 0)),
						new Finance("test3", "a", 10, LocalDateTime.of(2023, 6, 20, 0, 0, 0)))));
	}

	@AfterAll
	private void AfterAll() {
		// 自定義方法直接使用
		fDao.deleteReports("test1");
		fDao.deleteReports("test2");
		fDao.deleteReports("test3");
	}

	@Test
	void insertReportTest() {
		// 新增失敗
		Assert.isTrue(fDao.insertReport("test1", "d", 10, LocalDateTime.of(2023, 5, 20, 0, 0, 0)) == 0,
				RtnCode.TEST1_ERROR.getMessage());
		// 新稱成功
		Assert.isTrue(fDao.insertReport("test1", "x", 10, LocalDateTime.of(2023, 8, 20, 0, 0, 0)) == 1,
				RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void searchTitleTest() {
		// 尋找失敗
		Assert.isTrue(fDao.searchTitle(null, 7).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fDao.searchTitle("", 7).size() == 0, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fDao.searchTitle("test2", 13).size() == 0, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(fDao.searchTitle("test2", 7).size() == 0, RtnCode.TEST4_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fDao.searchTitle("test2", 6).size() == 1, RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void searchDetailTest() {
		// 尋找失敗
		Assert.isTrue(fDao.searchDetail(null, 7).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fDao.searchDetail("", 7).size() == 0, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fDao.searchDetail("b", 13).size() == 0, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(fDao.searchDetail("b", 7).size() == 0, RtnCode.TEST4_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fDao.searchDetail("d", 6).size() == 1, RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void searchMonthDataTest() {
		// 尋找失敗
		Assert.isTrue(fDao.searchMonthData(13).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fDao.searchMonthData(5).size() == 3, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void addReportTest() {
		// 輸入為空
		Assert.isTrue(fSer.addReport(null, "d", 10).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.addReport("test1", "", 10).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fSer.addReport("test1", "d", -10).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(fSer.addReport("vip", "vip", 980).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
		// 資料已存在
		Assert.isTrue(fSer.addReport("vip", "vip", 980).getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST6_ERROR.getMessage());
		fDao.deleteReports("vip");
	}

	@Test
	void findTitleTest() {
		// 尋找失敗
		Assert.isTrue(fSer.findTitle(null, 5).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.findTitle("", 5).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fSer.findTitle("test1", 15).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fSer.findTitle("test1", 5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void findDetailTest() {
		// 尋找失敗
		Assert.isTrue(fSer.findDetail(null, 5).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.findDetail("", 5).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fSer.findDetail("d", 15).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fSer.findDetail("d", 5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void monthlyReportTest() {
		// 尋找失敗
		Assert.isTrue(fSer.monthlyReport(0).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fSer.monthlyReport(5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void monthlyRatioTest() {
		// 尋找失敗
		Assert.isTrue(fSer.monthlyRatio(0).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fSer.monthlyRatio(5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
	}

}
