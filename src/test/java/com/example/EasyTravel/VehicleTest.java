package com.example.EasyTravel;

import java.time.LocalDateTime;

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
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.VehicleService;
import com.example.EasyTravel.vo.VehicleResponse;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VehicleTest {

	@Autowired
	private VehicleDao vDao;

	@Autowired
	private VehicleService vehicleService;


	@Autowired
	private VehicleDao vehicleDao;

	@BeforeEach
	private void BeforeEach() {
		// 建立假資料
		// 汽車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("AA-001", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "c1", "l1", 0, 50000),
				new Vehicle("AA-002", "sedan", 2000, LocalDate.now(), LocalDate.now(), "可租借", "c1", "l2", 0, 50000),
				new Vehicle("AA-003", "suv", 2000, LocalDate.now(), LocalDate.now(), "可租借", "c1", "l2", 0, 50000),
				new Vehicle("AA-004", "ven", 2000, LocalDate.now(), LocalDate.now(), "可租借", "c2", "l1", 0, 50000))));
		// 機車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("MX-01", "scooter", 50, LocalDate.now(), LocalDate.now(), "可租借", "c1", "l1", 0, 100),
				new Vehicle("MX-02", "motorcycle", 150, LocalDate.now(), LocalDate.now(), "可租借", "c2", "l1", 0, 300),
				new Vehicle("MX-03", "scooter", 100, LocalDate.now(), LocalDate.now(), "可租借", "c2", "l2", 0, 200),
				new Vehicle("MX-04", "heavy motorcycle", 550, LocalDate.now(), LocalDate.now(), "可租借", "c3", "l1", 0,
						2000))));
		// 腳踏車
		vDao.saveAll(new ArrayList<>(Arrays.asList(
				new Vehicle("CB0001", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "c3", "l1", 0, 50),
				new Vehicle("CB0002", "bike", 0, LocalDate.now(), LocalDate.now(), "可租借", "c3", "l1", 0, 50))));
	}

	@AfterAll
	private void AfterAll() {
		// 刪除假資料
		vDao.deleteAllById(new ArrayList<>(Arrays.asList("AA-001", "AA-002", "AA-003", "AA-004", "MX-01", "MX-02",
				"MX-03", "MX-04", "CB0001", "CB0002")));
	}

	@Test
	public void addCarTest() {
		VehicleResponse res1 = vehicleService.addCar("AZ-12345", "bike", 0, 200);
		System.out.println(res1.getMessage());
	}

	@Test
	public void updateCarInfoTest() {
		VehicleResponse res1 = vehicleService.updateCarInfo("AC-1234", -9.7, "已報廢");
		System.out.println(res1.getMessage());
	}

	@Test
	public void scrapCarTest() {
		VehicleResponse res1 = vehicleService.scrapCar("AH-1234");
		System.out.println(res1.getMessage());
	}

	@Test
	public void findCarByCategoryTest() {
		VehicleResponse res1 = vehicleService.findCarByCategory("bike");
		System.out.println(res1.getMessage());
	}

	@Test
	public void UpdateStatusTest() {
		String licensePlate = "AZ-1234";
		LocalDateTime latestCheckDate = LocalDateTime.of(2023, 6, 5, 15, 37, 3);
		String available = "可租借";

		int res = vehicleDao.UpdateStatus(licensePlate, latestCheckDate, available);
		Assert.isTrue(res == 1, RtnCode.TEST1_ERROR.getMessage());
	}
	
	@Test
	public void sortCategoryTest() {
		// 操作失敗
		Assert.isTrue(vDao.sortCategory(new ArrayList<>(Arrays.asList("test1", "test2", "", null))).size() == 0,
				RtnCode.TEST1_ERROR.getMessage());
		// 操作成功
		Assert.isTrue(vDao
				.sortCategory(new ArrayList<>(
						Arrays.asList("AA-001", "AA-002", "MX-01", "MX-02", "MX-03", "MX-04", "CB0001", "CB0002")))
				.size() == 5, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void dispatchTest() {
		// 操作失敗
		Assert.isTrue(vDao.dispatch(new ArrayList<>(Arrays.asList("test1", "test2", "", null)), "city1", "first") == 0,
				RtnCode.TEST1_ERROR.getMessage());
		// 操作成功 六筆成功六次
		Assert.isTrue(
				vDao.dispatch(new ArrayList<>(Arrays.asList("AA-001", "AA-002", "MX-01", "MX-02", "CB0001", "CB0002")),
						"city1", "first") == 6,
				RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void updateRentInfoTest() {
		// 操作失敗
		Assert.isTrue(vDao.updateRentInfo(null, "租借中", null, null, 0) == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(vDao.updateRentInfo("", "可租借", "city1", "first", 20) == 0, RtnCode.TEST2_ERROR.getMessage());
		// 操作成功
		Assert.isTrue(vDao.updateRentInfo("AA-001", "租借中", null, null, 0) == 1, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(vDao.updateRentInfo("AA-001", "可租借", "c1", "first", 250) == 1, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void searchVehiclesByCityLocationTest() {
		// 尋找失敗
		Assert.isTrue(vDao.searchVehiclesByCityLocation(null, null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(vDao.searchVehiclesByCityLocation("", "").size() == 0, RtnCode.TEST2_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(vDao.searchVehiclesByCityLocation("c1", "l1").size() == 2, RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(vDao.searchVehiclesByCityLocation("c2", "l2").size() == 1, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void searchVehicleCategoryTest() {
		// 搜尋失敗
		Assert.isTrue(vDao.searchVehicleCategory(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(vDao.searchVehicleCategory(new ArrayList<>(Arrays.asList(null, ""))).size() == 0,
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(vDao.searchVehicleCategory(new ArrayList<>(Arrays.asList("XX", "AA"))).size() == 0,
				RtnCode.TEST3_ERROR.getMessage());
		// 搜尋成功
		Assert.isTrue(vDao.searchVehicleCategory(new ArrayList<>(Arrays.asList("AA-001", "AA-002"))).size() == 2,
				RtnCode.TEST4_ERROR.getMessage());
		Assert.isTrue(vDao.searchVehicleCategory(new ArrayList<>(Arrays.asList("MX-04", "MX-05"))).size() == 1,
				RtnCode.TEST4_ERROR.getMessage());
	}
	

}
