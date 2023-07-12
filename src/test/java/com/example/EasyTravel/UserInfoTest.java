package com.example.EasyTravel;

import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.UserInfo;
import com.example.EasyTravel.repository.UserInfoDao;
import com.example.EasyTravel.service.ifs.UserInfoService;
import com.example.EasyTravel.vo.UserInfoRequest;
import com.example.EasyTravel.vo.UserInfoResponse;

@SpringBootTest(classes = EasyTravelApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserInfoTest {

	@Autowired
	public UserInfoDao uDao;

	@Autowired
	public UserInfoService userInfoService;

	@BeforeEach
	private void BeforeEach() {
		// 建立假資料
		uDao.saveAll(new ArrayList<>(Arrays.asList(
				new UserInfo("account1", "pwd1", "name1", LocalDate.of(2023, 6, 1), false, LocalDateTime.now(), false,
						false, false, null),
				new UserInfo("account2", "pwd2", "name2", LocalDate.of(2023, 6, 1), true, LocalDateTime.now(), false,
						false, false, null),
				new UserInfo("account3", "pwd3", "name3", LocalDate.of(2023, 6, 1), true, LocalDateTime.now(), true,
						true, false, null))));
	}

	@AfterAll
	private void AfterAll() {
		// 刪除假資料
		uDao.deleteAllById(new ArrayList<>(Arrays.asList("account1", "account2", "account3")));
	}

	@Test
	public void userRegistrationTest1() {
		// 防呆:帳號、密碼、名稱、生日不得為空
		UserInfoRequest request = new UserInfoRequest();
		request.setAccount("12345678");
		request.setPassword("1234565789");
		request.setName("路招搖");
		LocalDate birthday = null;
		request.setBirthday(birthday);

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userRegistrationTest2() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:帳號 -- 帳號長度 3~8 碼，不能有任何空白
		request.setAccount("12");
		request.setPassword("12345678");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userRegistrationTest3() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:密碼為8-12碼
		request.setAccount("12345678");
		request.setPassword("12");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userRegistrationTest4() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:重複註冊帳號
		request.setAccount("01234567");
		request.setPassword("0123456789");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userRegistrationTest5() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:帳號密碼不可以相同
		request.setAccount("12345678");
		request.setPassword("12345678");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userRegistrationTest6() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:生日不可以超過現在日期
		request.setAccount("12345678");
		request.setPassword("123456789");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userRegistration(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userActiveTest1() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:帳密不得為空或null
		request.setAccount("");
		request.setPassword("123456789");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userActive(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userActiveTest2() {
		UserInfoRequest request = new UserInfoRequest();
		// 成功激活
		request.setAccount("12345678");
		request.setPassword("123456789");
		request.setName("路招搖");
		request.setBirthday(LocalDate.of(1994, 9, 23));

		UserInfoResponse res = userInfoService.userActive(request);
		System.out.println(res.getMessage());
	}

	@Test
	void userLoginTest1() {
		UserInfoRequest request = new UserInfoRequest();
		// 防呆:帳密不得為空或null
		request.setAccount("");
		request.setPassword("123456789");
		
		UserInfoResponse res = userInfoService.userLogin(request);
		System.out.println(res.getMessage());
	}

	@Test
	public void checkAccountTest() {
		// 查詢失敗
		Assert.isTrue(uDao.checkAccount(null) == null, RtnCode.TEST1_ERROR.getMessage());
		Assert.isTrue(uDao.checkAccount("") == null, RtnCode.TEST2_ERROR.getMessage());
		Assert.isTrue(uDao.checkAccount("account1") == null, RtnCode.TEST3_ERROR.getMessage());
		// 查詢成功
		Assert.isTrue(!uDao.checkAccount("account2").isDrivingLicense(), RtnCode.TEST4_ERROR.getMessage());
		Assert.isTrue(uDao.checkAccount("account3").isMotorcycleLicense(), RtnCode.TEST5_ERROR.getMessage());
	}
		
	@Test
	public void userLoginTest2() {
		UserInfoRequest request = new UserInfoRequest();
		// 成功登入
		request.setAccount("12345678");
		request.setPassword("123456789");

		UserInfoResponse res = userInfoService.userLogin(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userInfoSearchTest() {
		UserInfoRequest request = new UserInfoRequest();
//		UserInfoController controller = new UserInfoController();
//		controller.userLogin(request, null);
		// 成功查詢
//		request.setAccount("12345678");
//		request.setPassword("123456789");

		UserInfoResponse res = userInfoService.userInfoSearch(request);
		System.out.println(res.getMessage());

	}

	@Test
	public void userInfoUpdateTest() {
		UserInfoRequest request = new UserInfoRequest();
		request.setAccount("12345678");
		request.setPassword("123456789");

		request.setBirthday(LocalDate.of(1999, 9, 23));
		request.setDrivingLicense(true);
		request.setMotorcycleLicense(true);
		UserInfoResponse res = userInfoService.userInfoUpdate(request);
		System.out.println(res.getMessage());
	}

	@Test
	public void userInfoUpgradeVIPTest() {
		UserInfoRequest request = new UserInfoRequest();
		request.setAccount("12345678");

		UserInfoResponse res = userInfoService.userInfoUpgradeVIP(request);
		System.out.println(res.getMessage());
	}

	@Test
	public void userInfoQuitVIPTest() {
		UserInfoRequest request = new UserInfoRequest();
		request.setAccount("12345678");

		UserInfoResponse res = userInfoService.userInfoQuitVIP(request);
		System.out.println(res.getMessage());
	}

//----------------------------------------
	@Test
	public void addBySql() {
		// 防呆:帳號、密碼、名稱、生日不得為空
		int sqlAdd = uDao.addBySql("a1234", "app", "name",LocalDate.now(), LocalDateTime.now());
//		System.out.println(sqlAdd);
		Assert.isTrue(sqlAdd == 1, RtnCode.TEST1_ERROR.getMessage());
	}
	
}
