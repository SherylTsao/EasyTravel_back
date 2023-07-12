package com.example.EasyTravel.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.UserInfo;
import com.example.EasyTravel.repository.UserInfoDao;
import com.example.EasyTravel.service.ifs.UserInfoService;
import com.example.EasyTravel.vo.UserInfoRequest;
import com.example.EasyTravel.vo.UserInfoResponse;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;
	private String patternAcc = "\\w{3,8}";
//	private String patternAcc = "";
	private String patternPwd = "[[\\w]&&[^\\s]]{8,12}";
//	private String patternPwd = "";

	@Override
	public UserInfoResponse userRegistration(UserInfoRequest request) {
		// 防呆:帳號、密碼、名稱、生日不得為空
		if (!StringUtils.hasText(request.getAccount()) || !StringUtils.hasText(request.getPassword())
				|| !StringUtils.hasText(request.getName()) || request.getBirthday() == null) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 防呆:帳號 -- 帳號長度 3~8 碼，不能有任何空白
		if (!request.getAccount().matches(patternAcc)) {
			return new UserInfoResponse(RtnCode.INCORRECT.getMessage());
		}
		// 防呆:密碼為8-12碼
		if (!request.getPassword().matches(patternPwd)) {
			return new UserInfoResponse(RtnCode.INCORRECT.getMessage());
		}
		// 防呆:重複註冊帳號
		if (userInfoDao.existsById(request.getAccount())) {
			return new UserInfoResponse(RtnCode.ALREADY_EXISTED.getMessage());
		}
		// 防呆:帳號密碼不可以相同
		if (request.getAccount().equals(request.getPassword())) {
			return new UserInfoResponse(RtnCode.CONFLICT.getMessage());
		}

		// 防呆:生日不可以超過現在日期
		if (request.getBirthday().isAfter(LocalDate.now())) {
			return new UserInfoResponse(RtnCode.INCORRECT.getMessage());
		}

//		UserInfo userInfo = new UserInfo();
		userInfoDao.addBySql(request.getAccount(), request.getPassword(), request.getName(), request.getBirthday(),
				LocalDateTime.now());
		return new UserInfoResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public UserInfoResponse userActive(UserInfoRequest request) {
		// 防呆:不得為空或null
		if (!StringUtils.hasText(request.getAccount()) || !StringUtils.hasText(request.getPassword())) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 自定義:從資料庫找到未激活的帳戶
		UserInfo userInfo = userInfoDao.findByAccountAndPasswordAndActive(request.getAccount(), request.getPassword(),
				false);
		userInfo.setActive(true);
		userInfoDao.save(userInfo);
		return new UserInfoResponse(RtnCode.SUCCESSFUL.getMessage());

	}

	@Override
	public UserInfoResponse userLogin(UserInfoRequest request) {

		// 防呆:不得為空或null
		if (!StringUtils.hasText(request.getAccount()) || !StringUtils.hasText(request.getPassword())) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		// 自定義方法:找到帳號密碼已生效
		UserInfo userInfo = userInfoDao.findByAccountAndPasswordAndActive(request.getAccount(), request.getPassword(),
				true);
		// optional才有isPresent&Empty
		if (userInfo == null) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
//userinfo加上去20230611
		return new UserInfoResponse(userInfo, RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public UserInfoResponse userInfoSearch(UserInfoRequest request) {

		// 自定義方法:找到帳號密碼已生效
		UserInfo userInfo = userInfoDao.findByAccountAndPasswordAndActive(request.getAccount(), request.getPassword(),
				true);
		// 資料庫沒有找到時
		if (userInfo == null) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}

		return new UserInfoResponse(userInfo, RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public UserInfoResponse userInfoUpdate(UserInfoRequest request) {

		// 自定義方法:找到帳號密碼已生效
		UserInfo userInfo = userInfoDao.findByAccountAndPasswordAndActive(request.getAccount(), request.getPassword(),
				true);
		// 資料庫沒有找到時
		if (userInfo == null) {
			return new UserInfoResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}

		// 防呆: 未滿18歲新增駕照 利用生日換算年齡**
		int oldYears = 0;
		LocalDate birt = request.getBirthday() != null ? request.getBirthday() : userInfo.getBirthday();
		oldYears = LocalDate.now().getYear() - birt.getYear();
		if (LocalDateTime.now().getMonthValue() < birt.getMonthValue()) {
			oldYears--;
		}
		if (LocalDateTime.now().getMonthValue() == birt.getMonthValue()) {
			if (LocalDateTime.now().getDayOfMonth() < userInfo.getBirthday().getDayOfMonth()) {
				oldYears--;
			}
		}

		System.err.println(oldYears);
		if ((oldYears < 18 && request.isDrivingLicense() == true) || (oldYears < 18 && request.isMotorcycleLicense())) {
			return new UserInfoResponse(RtnCode.OUT_OF_AGE.getMessage());
		}
		// 新增修改生日&駕照資訊
		userInfo.setBirthday(request.getBirthday() != null ? request.getBirthday() : userInfo.getBirthday());
		userInfo.setDrivingLicense(request.isDrivingLicense());
		userInfo.setMotorcycleLicense(request.isMotorcycleLicense());
		
		userInfoDao.save(userInfo);
		return new UserInfoResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public UserInfoResponse userInfoUpgradeVIP(UserInfoRequest request) {

//		確認是否一次繳200元,財務的表需要連結嗎

//		確認為一般會員
		Optional<UserInfo> userInfo = userInfoDao.findById(request.getAccount());
		if (userInfo.isEmpty()) {
			return new UserInfoResponse(RtnCode.NOT_FOUND.getMessage());
		}
		UserInfo vip = userInfo.get();
		
//		2023-06-15修改
//		防呆:已經是vip卻重複成為vip
		if(vip.isVip()== true) {
			return new UserInfoResponse(RtnCode.INCORRECT.getMessage());
		}
		
//			VIP轉成TRUE
		vip.setVip(true);
//		紀錄時間
		vip.setVipStartDate(LocalDate.now());
//		修改過0611
		userInfoDao.save(vip);
		return new UserInfoResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	@Override
	public UserInfoResponse userInfoQuitVIP(UserInfoRequest request) {
//		確認為VIP會員
		UserInfo userInfo = userInfoDao.findByAccountAndVip(request.getAccount(), true);
//		找不到會員
		if (userInfo == null) {
			return new UserInfoResponse(RtnCode.NOT_FOUND.getMessage());
		}
//			VIP轉成TRUE
		userInfo.setVip(false);
		userInfo.setVipStartDate(null);
		userInfoDao.save(userInfo);
		return new UserInfoResponse(RtnCode.SUCCESSFUL.getMessage());
	}

}
