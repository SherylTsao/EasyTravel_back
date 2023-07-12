package com.example.EasyTravel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.Rent;

@Repository
public interface RentDao extends JpaRepository<Rent, Integer> {

	/*
	 * 尋找用戶所有租借明細
	 */
	public List<Rent> findByAccount(String account);

	/*
	 * 尋找當下租借明細(最新)
	 */
	public Rent findTop1ByAccountOrderBySerialNumberDesc(String account);

	/*
	 * 尋找月份資料
	 */
	@Query(value = "select * from rent "
			+ "where month(now_time) = :month", nativeQuery = true)
	public List<Rent> searchMonthData(
			@Param("month") int month);
	
	/*
	 *  刪除資料
	 */
	@Transactional
	@Modifying
	@Query("delete Rent r "
			+ "where r.account = :account and r.licensePlate = :licensePlate")
	public int deleteRent(
			@Param("account") String account,
			@Param("licensePlate") String licensePlate);
}
