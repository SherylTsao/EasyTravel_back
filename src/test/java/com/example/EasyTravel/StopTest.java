package com.example.EasyTravel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Stop;
import com.example.EasyTravel.entity.StopId;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.StopDao;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.StopService;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StopTest {

	@Autowired
	private StopDao sDao;

	@Autowired
	private StopService sSer;

	@Autowired
	private VehicleDao vDao;

	@BeforeEach
	private void BeforeEach() {
		// 建立假資料
		sDao.saveAll(new ArrayList<>(Arrays.asList(
				new Stop("city1", "first", 10, 0, 0), 
				new Stop("city1", "Second", 10, 0, 0),
				new Stop("city2", "first", 0, 0, 0), 
				new Stop("city2", "Second", 10, 10, 10))));
		// 汽車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("AA-001", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 50000), 
				new Vehicle("AA-002", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 50000),
				new Vehicle("AA-003", "suv", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 50000), 
				new Vehicle("AA-004", "ven", 2000, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 50000))));
		// 機車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("MX-01", "scooter", 50, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 100),
				new Vehicle("MX-02", "motorcycle", 150, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 300), 
				new Vehicle("MX-03", "scooter", 100, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 200),
				new Vehicle("MX-04", "heavy motorcycle", 550, LocalDate.now(), LocalDate.now(), "可租借", "city2", "Second", 0, 2000))));
		// 腳踏車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("CB0001", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "city1", "first", 0, 50), 
				new Vehicle("CB0002", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "city1", "first", 0, 50))));
	}

	@AfterAll
	private void AfterAll() {
		// 刪除假資料
		sDao.deleteAllById(new ArrayList<>(Arrays.asList(
				new StopId("city1", "first"), 
				new StopId("city1", "Second"),
				new StopId("city2", "first"), 
				new StopId("city2", "Second"))));
		vDao.deleteAllById(new ArrayList<>(Arrays.asList(
				"AA-001", "AA-002", "AA-003", "AA-004", "MX-01", "MX-02", "MX-03", "MX-04", "CB0001", "CB0002")));
	}

	@Test
	void insertStopTest() {
		// 新增資料已存在
		Assert.isTrue(sDao.insertStop("city1", "first", 0, 0, 0) == 0, RtnCode.TEST1_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(sDao.insertStop("city3", "first", 0, 0, 0) == 1, RtnCode.TEST2_ERROR.getMessage());
		sDao.deleteById(new StopId("city3", "first"));
	}

	@Test
	void searchStopsTest() {
		// 尋找失敗
		Assert.isTrue(sDao.searchStops(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sDao.searchStops("").size() == 0, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(sDao.searchStops("city3").size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(sDao.searchStops("city2").size() == 2, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void updateBikeTest() {
		// 租、還失敗
		Assert.isTrue(sDao.plusBike(null, "") == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sDao.minusBike("city2", "first") == 0, RtnCode.TEST2_ERROR.getMessage());
		// 租、還成功
		Assert.isTrue(sDao.plusBike("city2", "first") == 1, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(sDao.minusBike("city2", "first") == 1, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void updateMotorcycleTest() {
		// 租、還失敗
		Assert.isTrue(sDao.plusMotorcycle(null, "") == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sDao.minusMotorcycle("city2", "first") == 0, RtnCode.TEST2_ERROR.getMessage());
		// 租、還成功
		Assert.isTrue(sDao.plusMotorcycle("city2", "first") == 1, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(sDao.minusMotorcycle("city2", "first") == 1, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void updateCarTest() {
		// 租、還失敗
		Assert.isTrue(sDao.plusCar(null, "") == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sDao.minusCar("city2", "first") == 0, RtnCode.TEST2_ERROR.getMessage());
		// 租、還成功
		Assert.isTrue(sDao.plusCar("city2", "first") == 1, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(sDao.minusCar("city2", "first") == 1, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void addStopTest() {
		// 輸入為空
		Assert.isTrue(sSer.addStop(null, "", 0, 0, 0).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sSer.addStop("city1", "", 0, 0, 0).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(sSer.addStop("city1", "Third", -5, 0, 0).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 資料已存在
		Assert.isTrue(
				sSer.addStop("city1", "Second", 1, 0, 0).getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(sSer.addStop("city1", "Third", 10, 10, 10).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
		sDao.deleteById(new StopId("city1", "Third"));
	}
	
	@Test
	void showAllStopsTest() {
		// 找尋成功
		Assert.isTrue(sSer.showAllStops().getStopList().size() == 4, RtnCode.TEST1_ERROR.getMessage());
	}

	@Test
	void findCityStopsTest() {
		// 尋找失敗
		Assert.isTrue(sSer.findCityStops(null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sSer.findCityStops("").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(sSer.findCityStops("city3").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(sSer.findCityStops("city2").getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}
	
	@Test
	void findStopsVehiclesTest() {
		// 尋找失敗
		Assert.isTrue(sSer.findStopsVehicles(null, "").getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(sSer.findStopsVehicles("city2", "Second").getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	void renewLimitTest() {
		// 更新失敗
		Assert.isTrue(sSer.renewAmount(null, null, 0, 0, 0).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(sSer.renewAmount("city3", "First", 20, 30, 5).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 更新成功
		Assert.isTrue(sSer.renewAmount("city2", "Second", -5, -7, -9).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(sSer.renewAmount("city1", "First", 20, 30, 5).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	void RentOrReturnTest() {
		// 借車失敗
		Assert.isTrue(sSer.rentOrReturn(true, null, null, null).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(
				sSer.rentOrReturn(true, "bike", "city3", "First").getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(sSer.rentOrReturn(true, "scooter", "city2", "First").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 還車成功
		Assert.isTrue(sSer.rentOrReturn(false, "sedan", "city2", "Second").getMessage()
				.equals(RtnCode.SUCCESSFUL.getMessage()), RtnCode.TEST4_ERROR.getMessage());
		// 租車成功
		Assert.isTrue(
				sSer.rentOrReturn(true, "bike", "city2", "Second").getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST5_ERROR.getMessage());
	}

	@Test
	void dispatchTest() {
		// 未登錄車輛
		Assert.isTrue(sSer.dispatch(new ArrayList<>(Arrays.asList("", null)), "city1", "first").getMessage()
				.equals(RtnCode.INCORRECT.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		// 轉移成功
		Assert.isTrue(
				sSer.dispatch(new ArrayList<>(Arrays.asList("MX-03", "MX-04", "CB0001", "CB0002")), "city1", "first")
						.getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
	}

}
