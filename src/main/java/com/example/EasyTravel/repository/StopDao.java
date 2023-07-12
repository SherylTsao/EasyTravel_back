package com.example.EasyTravel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.Stop;
import com.example.EasyTravel.entity.StopId;

@Repository
public interface StopDao extends JpaRepository<Stop, StopId> {

	/*
	 * 新增資料
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into stop (city, location, bike_amount, motorcycle_amount, car_amount) "
			+ "select :city, :location, :bikeAmount, :motorcycleAmount, :carAmount "
			+ "where not exists (select 1 from stop "
			+ "where city = :city and location = :location)", nativeQuery = true)
	public int insertStop(
			@Param("city") String city, 
			@Param("location") String location,
			@Param("bikeAmount") int bikeAmount, 
			@Param("motorcycleAmount") int motorcycleAmount,
			@Param("carAmount") int carAmount);

	/*
	 * 尋找站點
	 */
	@Query(value = "select * from stop "
			+ "where city = :city", nativeQuery = true)
	public List<Stop> searchStops(
			@Param("city") String city);

	/*
	 * 更新站點可借車輛-租-腳踏車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.bikeAmount = s.bikeAmount - 1 "
			+ "where s.city = :city and s.location = :location and s.bikeAmount > 0")
	public int minusBike(
			@Param("city") String city, 
			@Param("location") String location);

	/*
	 * 更新站點可借車輛-租-機車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.motorcycleAmount = s.motorcycleAmount - 1 "
			+ "where s.city = :city and s.location = :location and s.motorcycleAmount > 0")
	public int minusMotorcycle(
			@Param("city") String city, 
			@Param("location") String location);

	/*
	 * 更新站點可借車輛-租-車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.carAmount = s.carAmount - 1 "
			+ "where s.city = :city and s.location = :location and s.carAmount > 0")
	public int minusCar(
			@Param("city") String city, 
			@Param("location") String location);

	/*
	 * 更新站點可借車輛-還-腳踏車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.bikeAmount = s.bikeAmount + 1 "
			+ "where s.city = :city and s.location = :location")
	public int plusBike(
			@Param("city") String city, 
			@Param("location") String location);

	/*
	 * 更新站點可借車輛-還-機車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.motorcycleAmount = s.motorcycleAmount + 1 "
			+ "where s.city = :city and s.location = :location")
	public int plusMotorcycle(
			@Param("city") String city, 
			@Param("location") String location);

	/*
	 * 更新站點可借車輛-還-車
	 */
	@Transactional
	@Modifying
	@Query("update Stop s set s.carAmount = s.carAmount + 1 "
			+ "where s.city = :city and s.location = :location")
	public int plusCar(
			@Param("city") String city, 
			@Param("location") String location);

}
