package com.example.EasyTravel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.Finance;

@Repository
public interface FinanceDao extends JpaRepository<Finance, Integer> {

	/*
	 *  新增資料
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into finance (title, detail, price, build_time) "
			+ "select :title, :detail, :price, :buildTime "
			+ "where not exists (select 1 from finance "
			+ "where title = :title and detail = :detail and build_time = :buildTime)", nativeQuery = true)
	public int insertReport(
			@Param("title") String title, 
			@Param("detail") String detail, 
			@Param("price") int price,
			@Param("buildTime") LocalDateTime buildTime);

	/*
	 *  尋找同title資料
	 */
	@Query(value = "select * from finance "
			+ "where title = :title and month(build_time) = :month", nativeQuery = true)
	public List<Finance> searchTitle(
			@Param("title") String title, 
			@Param("month") int month);

	/*
	 *  尋找同detail資料
	 */
	@Query(value = "select * from finance "
			+ "where detail = :detail and month(build_time) = :month", nativeQuery = true)
	public List<Finance> searchDetail(
			@Param("detail") String detail, 
			@Param("month") int month);

	/*
	 *  尋找月份資料
	 */
	@Query(value = "select * from finance "
			+ "where month(build_time) = :month", nativeQuery = true)
	public List<Finance> searchMonthData(
			@Param("month") int month);

	/*
	 *  刪除資料
	 */
	@Transactional
	@Modifying
	@Query("delete Finance f "
			+ "where f.title = :title")
	public int deleteReports(
			@Param("title") String title);
	
}
