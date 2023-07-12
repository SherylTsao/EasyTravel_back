package com.example.EasyTravel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Fee;
import com.example.EasyTravel.entity.Rent;
import com.example.EasyTravel.entity.Stop;
import com.example.EasyTravel.entity.StopId;
import com.example.EasyTravel.entity.UserInfo;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.FeeDao;
import com.example.EasyTravel.repository.RentDao;
import com.example.EasyTravel.repository.StopDao;
import com.example.EasyTravel.repository.UserInfoDao;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.RentService;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentTests {

	@Autowired
	private StopDao sDao;

	@Autowired
	private UserInfoDao uDao;

	@Autowired
	private VehicleDao vDao;

	@Autowired
	private FeeDao fDao;

	@Autowired
	private RentDao rDao;

	@Autowired
	private RentService rSer;

	@BeforeAll
	private void BeforeAll() {
		// 建立假資料
		// 1.站點
		sDao.saveAll(new ArrayList<>(
				Arrays.asList(new Stop("city1", "first", 10, 0, 0), new Stop("city1", "Second", 10, 0, 0),
						new Stop("city2", "first", 0, 0, 0), new Stop("city2", "Second", 10, 10, 10))));
		// 2.帳號
		uDao.saveAll(new ArrayList<>(Arrays.asList(
				new UserInfo("account1", "pwd1", "name1", LocalDate.of(2023, 6, 1), false, LocalDateTime.now(), false,
						false, false, null),
				new UserInfo("account2", "pwd2", "name2", LocalDate.of(2023, 6, 1), true, LocalDateTime.now(), false,
						false, false, null),
				new UserInfo("account3", "pwd3", "name3", LocalDate.of(2023, 6, 1), true, LocalDateTime.now(), true,
						true, false, null),
				new UserInfo("account4", "pwd4", "name4", LocalDate.of(2023, 6, 1), true, LocalDateTime.now(), true,
						true, false, null))));
		// 3.交通工具
		// 汽車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("AA-001", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0,
						50000),
				new Vehicle("AA-002", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0,
						50000),
				new Vehicle("AA-003", "suv", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 50000),
				new Vehicle("AA-004", "ven", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0,
						50000))));
		// 機車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("MX-01", "scooter", 50, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 100),
				new Vehicle("MX-02", "motorcycle", 150, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0,
						300),
				new Vehicle("MX-03", "scooter", 100, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 200),
				new Vehicle("MX-04", "heavy motorcycle", 550, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second",
						0, 2000))));
		// 腳踏車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("CB0001", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "city1", "first", 0, 50),
				new Vehicle("CB0002", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "city1", "first", 0, 50))));
		// 4.費率
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("bike", 0, 0.0, 30), new Fee("bike", 0, 0.33, 240),
				new Fee("bike", 0, 0.66, 480), new Fee("bike", 0, 1, 1440), new Fee("bike", 0, 2, 9999))));
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("sedan", 1200, 2000, 1), new Fee("sedan", 1200, 1800, 30),
				new Fee("sedan", 1200, 1500, 365))));
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("sedan", 2000, 3500, 1), new Fee("sedan", 2000, 3200, 30),
				new Fee("sedan", 2000, 2800, 365))));
		// 5.租借明細
		rDao.saveAll(new ArrayList<>(Arrays.asList(new Rent("account2", "CB0001", "city1", "first"),
				new Rent("account3", "AA-001", "city2", "Second"),
				new Rent("account3", "AA-001", "city1", "Second", false, 2400))));
	}

	@AfterAll
	private void AfterAll() {
		// 刪除假資料
		// 1.站點
		sDao.deleteAllById(new ArrayList<>(Arrays.asList(new StopId("city1", "first"), new StopId("city1", "Second"),
				new StopId("city2", "first"), new StopId("city2", "Second"))));
		// 2.帳號
		uDao.deleteAllById(new ArrayList<>(Arrays.asList("account1", "account2", "account3")));
		// 3.交通工具
		vDao.deleteAllById(new ArrayList<>(Arrays.asList("AA-001", "AA-002", "AA-003", "AA-004", "MX-01", "MX-02",
				"MX-03", "MX-04", "CB0001", "CB0002")));
		// 4.費率
		fDao.deleteProjects("bike", 0);
		fDao.deleteProjects("sedan");
		// 5. 租借明細
		rDao.deleteRent("account2", "CB0001");
		rDao.deleteRent("account3", "AA-001");
	}

	@Test
	void findByAccountTest() {
		// 查詢失敗
		Assert.isTrue(rDao.findByAccount(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(rDao.findByAccount("").size() == 0, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(rDao.findByAccount("account0").size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// 查尋成功
		Assert.isTrue(rDao.findByAccount("account3").size() == 2, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void findTop1ByAccountAndLicensePlateOrderBySerialNumberDescTest() {
		// 查詢失敗
		Assert.isTrue(rDao.findTop1ByAccountOrderBySerialNumberDesc(null) == null, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(rDao.findTop1ByAccountOrderBySerialNumberDesc("") == null, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(rDao.findTop1ByAccountOrderBySerialNumberDesc("account0") == null,
				RtnCode.TEST3_ERROR.getMessage());
		// 查尋成功
		Assert.isTrue(rDao.findTop1ByAccountOrderBySerialNumberDesc("account2").getLicensePlate().equals("CB0001"),
				RtnCode.TEST4_ERROR.getMessage());
		Assert.isTrue(rDao.findTop1ByAccountOrderBySerialNumberDesc("account3").getPrice() == 2400,
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void searchMonthDataTest() {
		// 尋找失敗
		Assert.isTrue(rDao.searchMonthData(13).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(rDao.searchMonthData(6).size() != 0, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void rentTest() {
		// 租借失敗，findById方法不能輸入null
		Assert.isTrue(rSer.rent(null, "").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(rSer.rent("account1", "CB0001").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 無駕照
		Assert.isTrue(rSer.rent("account2", "AA-001").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 成功
		Assert.isTrue(rSer.rent("account4", "AA-001").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		rDao.deleteRent("account4", "AA-001");
	}

	@Test
	void dropOffTest() {
		// 租借失敗，findById方法不能輸入null
		Assert.isTrue(
				rSer.dropOff(null, "", "city1", "location", 100).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(rSer.dropOff("account3", "AA-001", "city1", "location", 100).getMessage()
				.equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 無此站點
		Assert.isTrue(rSer.dropOff("account2", "CB0001", "city5", "first", 0).getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 成功
		Assert.isTrue(rSer.dropOff("account2", "CB0001", "city1", "Second", 5).getMessage()
				.equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());

	}

	@Test
	void showDetailsTest() {
		// 尋找失敗
		Assert.isTrue(rSer.showDetails(null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(rSer.showDetails("").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(rSer.showDetails("account1").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(rSer.showDetails("account3").getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}
}
