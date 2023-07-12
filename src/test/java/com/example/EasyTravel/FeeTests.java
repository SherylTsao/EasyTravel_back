package com.example.EasyTravel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Fee;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.FeeDao;
import com.example.EasyTravel.service.ifs.FeeService;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FeeTests {

	@Autowired
	private FeeDao fDao;

	@Autowired
	private FeeService fSer;

	@BeforeAll
	private void BeforeAll() {
		// 建立假資料
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("bike", 0, 0.0, 30), new Fee("bike", 0, 0.33, 240),
				new Fee("bike", 0, 0.66, 480), new Fee("bike", 0, 1, 1440), new Fee("bike", 0, 2, 9999))));
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("sedan", 1200, 2000, 1), new Fee("sedan", 1200, 1800, 30),
				new Fee("sedan", 1200, 1500, 365))));
		fDao.saveAll(new ArrayList<>(Arrays.asList(new Fee("sedan", 2000, 3500, 1), new Fee("sedan", 2000, 3200, 30),
				new Fee("sedan", 2000, 2800, 365))));
	}

	@AfterAll
	private void AfterAll() {
		// 自定義方法直接使用
		fDao.deleteProjects("bike", 0);
		fDao.deleteProjects("sedan");
	}

	@Test
	public void insertProjectTest() {
		// 已存在
		Assert.isTrue(fDao.insertProject("bike", 0, 0.0, 30) == 0, RtnCode.TEST1_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(fDao.insertProject("sedan", 0, 0.0, 30) == 1, RtnCode.TEST2_ERROR.getMessage());
	}

	@Test
	public void deleteProjectDaoTest() {
		fDao.save(new Fee("suv", 0, 0.0, 30));
		// 輸入空白
		Assert.isTrue(fDao.deleteProject(null, 0, 0.0) == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fDao.deleteProject("", 0, 0.0) == 0, RtnCode.TEST2_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(fDao.deleteProject("suv", 0, 0.0) == 1, RtnCode.TEST3_ERROR.getMessage());
	}

	@Test
	public void searchProjectsByCcUpTest() {
		// 尋找失敗
		Assert.isTrue(fDao.searchProjectsByCcUp(null).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fDao.searchProjectsByCcUp("").size() == 11, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fDao.searchProjectsByCcUp("testX").size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fDao.searchProjectsByCcUp("sedan").size() == 6, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void searchProjectsByThresholdUpTest() {
		// 尋找失敗
		Assert.isTrue(fDao.searchProjectsByThresholdUp(null, 0).size() == 0, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fDao.searchProjectsByThresholdUp("", 0).size() == 5, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fDao.searchProjectsByThresholdUp("bike", -20).size() == 0, RtnCode.TEST3_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fDao.searchProjectsByThresholdUp("sedan", 1200).size() == 3, RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void addProjectTest() {
		// 參數空白
		Assert.isTrue(fSer.addProject(null, 10, 10, 10).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.addProject("suv", -1, 0, 0).getMessage().equals(RtnCode.CANNOT_EMPTY.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 已存在
		Assert.isTrue(fSer.addProject("bike", 0, 0.0, 30).getMessage().equals(RtnCode.ALREADY_EXISTED.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 新增成功
		Assert.isTrue(fSer.addProject("suv", 0, 0.0, 30).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		fDao.deleteProject("suv", 0, 0.0);
	}

	@Test
	public void deleteProjectTest() {
		// 參數空白
		Assert.isTrue(fSer.deleteProject(null, 10, 10.0).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.deleteProject(null, 10, -1.0).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(fSer.deleteProject(null, -1, -1.0).getMessage().equals(RtnCode.INCORRECT.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		// 成功刪除
		fDao.save(new Fee("suv", 0, 0.0, 30));
		Assert.isTrue(fSer.deleteProject("suv", -1, -1.0).getMessage().equals(RtnCode.SUCCESSFUL.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void findProjectsTest() {
		// 尋找失敗
		Assert.isTrue(fSer.findProjects(null, 0).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.findProjects(null, -1).getMessage().equals(RtnCode.NOT_FOUND.getMessage()),
				RtnCode.TEST2_ERROR.getMessage());
		// 尋找成功
		Assert.isTrue(fSer.findProjects("sedan", -1).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST3_ERROR.getMessage());
		Assert.isTrue(fSer.findProjects("sedan", 2000).getMessage().equals(RtnCode.SUCCESS.getMessage()),
				RtnCode.TEST4_ERROR.getMessage());
	}

	@Test
	public void calculateTest() {
		// 輸入為空
		Assert.isTrue(fSer.calculate(new Vehicle("CB0001", "bike", 0, 50), false, null).getMessage()
				.equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(fSer.calculate(null, false, Duration.ofHours(2).plusMinutes(30).plusSeconds(45)).getMessage()
				.equals(RtnCode.CANNOT_EMPTY.getMessage()), RtnCode.TEST2_ERROR.getMessage());
		// 方案未存在
		Assert.isTrue(fSer
				.calculate(new Vehicle("CB0001", "boat", 0, 50), false,
						Duration.ofHours(2).plusMinutes(30).plusSeconds(45))
				.getMessage().equals(RtnCode.NOT_FOUND.getMessage()), RtnCode.TEST3_ERROR.getMessage());
		// 成功計價
		Assert.isTrue(
				fSer.calculate(new Vehicle("CB0001", "bike", 0, 50), false, Duration.ofMinutes(450)).getTotal() == 208,
				RtnCode.TEST4_ERROR.getMessage());
		Assert.isTrue(fSer.calculate(new Vehicle("CB0001", "bike", 0, 50), false, Duration.ofMinutes(1430))
				.getTotal() == 1178, RtnCode.TEST5_ERROR.getMessage());
		Assert.isTrue(fSer.calculate(new Vehicle("AA-001", "sedan", 2000, 50000), false, Duration.ofDays(41))
				.getTotal() == 131200, RtnCode.TEST6_ERROR.getMessage());
	}
	
	@Test
	public void findAllTest() {
		List<Fee> res = fDao.findAll();
		for (Fee item : res) {
			System.out.println(item.getSerialNumber());
		}
	}
}
