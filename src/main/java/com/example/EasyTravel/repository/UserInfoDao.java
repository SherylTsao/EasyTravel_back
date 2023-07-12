package com.example.EasyTravel.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.UserInfo;

@Repository
public interface UserInfoDao extends JpaRepository<UserInfo, String> {

	public UserInfo findByAccountAndPasswordAndActive(String account, String password, boolean active);

	public UserInfo findByAccountAndVip(String account, boolean VIP);

	// 新增資料
	@Transactional
	@Modifying
	@Query(value = "insert into user_info (account, password, name, birthday, reg_time) values (:account, :password, :name, :birthday, :regTime)", nativeQuery = true)
	public int addBySql(@Param("account") String account, @Param("password") String password,
			@Param("name") String name, @Param("birthday") LocalDate birthday, @Param("regTime") LocalDateTime regtime);

	/*
	 * 取得帳號、駕照及vip資訊
	 */
	@Query("select new UserInfo(u.account, u.motorcycleLicense, u.drivingLicense, u.vip) from UserInfo u "
			+ "where u.account = :account and u.active = true")
	public UserInfo checkAccount(
			@Param("account") String account);
}
