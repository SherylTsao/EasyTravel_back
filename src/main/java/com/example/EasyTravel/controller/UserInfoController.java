package com.example.EasyTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EasyTravel.service.ifs.UserInfoService;
import com.example.EasyTravel.vo.UserInfoRequest;
import com.example.EasyTravel.vo.UserInfoResponse;

@CrossOrigin
@RestController
public class UserInfoController {

	@Autowired // 指定託管類別
	private UserInfoService userInfoService;

	// 會員註冊:連結外部
	@PostMapping(value = "user_registration")
	public UserInfoResponse userRegistration(@RequestBody UserInfoRequest request) {
		// 連結內部
		return userInfoService.userRegistration(request);
	}

	// 會員激活帳號:連結外部
	@PostMapping(value = "user_active")
	public UserInfoResponse userActive(@RequestBody UserInfoRequest request) {
		// 連結內部
		return userInfoService.userActive(request);
	}

	// 會員登入:連結外部
	@PostMapping(value = "user_login")
	public UserInfoResponse userLogin(@RequestBody UserInfoRequest request) {

//		session.setAttribute("Account", request.getAccount());
//		session.setAttribute("Password", request.getPassword());
//		session.setMaxInactiveInterval(900);
		// 取出session帳密
		// 連結內部
		return userInfoService.userLogin(request);

	}

	// 會員資料搜尋:連結外部
	@PostMapping(value = "user_info_search")
	public UserInfoResponse userInfoSearch(@RequestBody UserInfoRequest request) {
		// 取出session帳密
//		account,password改成大寫20230611
//		String account = (String) session.getAttribute("Account");
//		String pwd = (String) session.getAttribute("Password");
//		// 帳密不得為空,當沒有login過,或是時效過期,則為空
//		if (!StringUtils.hasText(account) || !StringUtils.hasText(pwd)) {
//			return new UserInfoResponse(RtnCode.OUT_OF_TIME.getMessage());
//		}
//		request.setAccount(account);
//		request.setPassword(pwd);

		// 連結內部
		return userInfoService.userInfoSearch(request);

	}

	// 會員修改資料:連結外部
	@PostMapping(value = "user_info_update")
	public UserInfoResponse userInfoUpdate(@RequestBody UserInfoRequest request) {
		// 取出session帳密
//		String account = (String) session.getAttribute("account");
//		String pwd = (String) session.getAttribute("pwd");
//		// 帳密不得為空,當沒有login過,或是時效過期,則為空
//		if (!StringUtils.hasText(account) || !StringUtils.hasText(pwd)) {
//			return new UserInfoResponse(RtnCode.OUT_OF_TIME.getMessage());
//		}
//		request.setAccount(account);
//		request.setPassword(pwd);

		// 連結內部
		return userInfoService.userInfoUpdate(request);

	}

	// 會員升級:連結外部
	@PostMapping(value = "user_info_upgrade_vip")
	public UserInfoResponse userInfoUpgradeVIP(@RequestBody UserInfoRequest request) {
		// 連結內部
		return userInfoService.userInfoUpgradeVIP(request);

	}

	// 會員升級:連結外部
	@PostMapping(value = "user_info_Quit_vip")
	public UserInfoResponse userInfoQuitVIP(@RequestBody UserInfoRequest request) {
		// 連結內部
		return userInfoService.userInfoQuitVIP(request);

	}
}
