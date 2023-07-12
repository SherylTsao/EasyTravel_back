package com.example.EasyTravel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.vo.VehicleCount;

@Repository
public interface VehicleDao extends JpaRepository<Vehicle, String> {

	public List<Vehicle> findAllByCategory(String category);
	
	/*
	 * 維修中更改車輛可租借狀態
	 */
	@Transactional
	@Modifying 
	@Query(value = "update vehicle set status = :status, latest_check_date = :latestCheckDate "			
			+ "where license_plate = :licensePlate", nativeQuery = true)
	public int UpdateStatus(@Param("licensePlate") String licensePlate, @Param("latestCheckDate") LocalDateTime latestCheckDate,@Param("status") String status);

	/*
	 * 依資料車種分類
	 */
	@Query("select new com.example.EasyTravel.vo.VehicleCount (v.category, count(v)) from Vehicle v "
			+ "where v.licensePlate in :vList group by v.category")
	public List<VehicleCount> sortCategory(
			@Param("vList") List<String> vehicleList);

	/*
	 * 更改多車輛的位置
	 */
	@Transactional
	@Modifying
	@Query(value = "update vehicle set city = :city, location = :location "
			+ "where license_plate in :vList", nativeQuery = true)
	public int dispatch(
			@Param("vList") List<String> vehicleList, 
			@Param("city") String city,
			@Param("location") String location);

	/*
	 * 更改車輛租借狀態
	 */
	@Transactional
	@Modifying
	@Query(value = "update vehicle set status = :status, city = :city, location = :location, odo = odo + :odo "
			+ "where license_plate = :licensePlate", nativeQuery = true)
	public int updateRentInfo(
			@Param("licensePlate") String licensePlate, 
			@Param("status") String status,
			@Param("city") String city, 
			@Param("location") String location, 
			@Param("odo") double odo);
	
	/*
	 * 取得站點的所有車輛
	 */
	@Query(value="select * from vehicle "
			+ "where city = :city and location = :location", nativeQuery = true)
	public List<Vehicle> searchVehiclesByCityLocation(
			@Param("city") String city,
			@Param("location") String location);
	
	/*
	 * 取得目標車輛的種類
	 */
	@Query("select new Vehicle(v.licensePlate, v.category) from Vehicle v "
			+ "where licensePlate in :vList")
	public List<Vehicle> searchVehicleCategory(
			@Param("vList") List<String> vehicleList);
	
	/*
	 * 取得接近報廢年限的車輛
	 */
	@Query(value="select * from vehicle where "
			+ "(category = 'bike' and start_serving_date <= DATE_SUB(CURRENT_DATE, INTERVAL 6 YEAR)) or "
			+ "(category regexp 'scooter|motorcycle|heavy motorcycle' and "
			+ "(start_serving_date <= DATE_SUB(CURRENT_DATE, INTERVAL 9 YEAR) or odo >= 119800)) or "
			+ "(category regexp 'sedan|ven|suv' and "
			+ "(start_serving_date <= DATE_SUB(CURRENT_DATE, INTERVAL 14 YEAR) OR odo >= 599000))", nativeQuery = true)
	public List<Vehicle> searchVehicleNearScrap();
	
	/*
	 * 取得年度未檢修的車輛
	 */
	@Query(value="select * from vehicle "
			+ "where latest_check_date <= DATE_SUB(CURRENT_DATE, INTERVAL 1 YEAR))", nativeQuery = true)
	public List<Vehicle> searchVehicleNeedCheck();
	
	/*
	 * 尋找在表上確實有該車牌的車輛
	 */
	@Query(value = "select * from vehicle where license_plate = :licensePlate", nativeQuery = true)
    public Vehicle getVehicleByLicensePlate(@Param("licensePlate") String licensePlate);
	
	
	
}
