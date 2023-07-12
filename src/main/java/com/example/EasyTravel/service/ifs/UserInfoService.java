package com.example.EasyTravel.service.ifs;

import com.example.EasyTravel.vo.UserInfoRequest;
import com.example.EasyTravel.vo.UserInfoResponse;

public interface UserInfoService {

	// 方法1:會員註冊
	public UserInfoResponse userRegistration(UserInfoRequest request);

	// 方法2:帳號啟用*要加session
	public UserInfoResponse userActive(UserInfoRequest request);

	// 方法3:會員登入
	public UserInfoResponse userLogin(UserInfoRequest request);

	// 方法4:會員資訊查詢
	public UserInfoResponse userInfoSearch(UserInfoRequest request);

	// 方法4:會員資訊修改
	public UserInfoResponse userInfoUpdate(UserInfoRequest request);

	// 方法5:會員升級VIP
	public UserInfoResponse userInfoUpgradeVIP(UserInfoRequest request);
	
	// 方法6:退VIP
	public UserInfoResponse userInfoQuitVIP(UserInfoRequest request);

}
