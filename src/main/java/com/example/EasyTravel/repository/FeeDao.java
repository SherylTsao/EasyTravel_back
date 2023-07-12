package com.example.EasyTravel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.EasyTravel.entity.Fee;

@Repository
public interface FeeDao extends JpaRepository<Fee, Integer> {

	/*
	 *  新增資料
	 */
	@Transactional
	@Modifying
	@Query(value = "insert into fee (project, cc, rate, threshold) "
			+ "select :project, :cc, :rate, :threshold "
			+ "where not exists (select 1 from fee "
			+ "where project = :project and cc = :cc and rate = :rate)", nativeQuery = true)
	public int insertProject(
			@Param("project") String project, 
			@Param("cc") int cc, 
			@Param("rate") double rate,
			@Param("threshold") int threshold);

	/*
	 *  刪除單筆資料
	 */
	@Transactional
	@Modifying
	@Query("delete Fee f "
			+ "where f.project = :project and f.cc = :cc and f.rate = :rate")
	public int deleteProject(
			@Param("project") String project, 
			@Param("cc") int cc, 
			@Param("rate") double rate);

	/*
	 *  刪除系列資料
	 */
	@Transactional
	@Modifying
	@Query("delete Fee f "
			+ "where f.project = :project and f.cc = :cc")
	public int deleteProjects(
			@Param("project") String project, 
			@Param("cc") int cc);

	/*
	 *  刪除車種資料
	 */
	@Transactional
	@Modifying
	@Query("delete Fee f "
			+ "where f.project = :project")
	public int deleteProjects(
			@Param("project") String project);

	/*
	 *  尋找同種交通工具費率
	 */
	@Query(value = "select * from fee "
			+ "where project = :project "
			+ "order by cc", nativeQuery = true)
	public List<Fee> searchProjectsByCcUp(
			@Param("project") String project);

	/*
	 *  尋找單一車種費率
	 */
	@Query(value = "select * from fee "
			+ "where project = :project and cc = :cc "
			+ "order by threshold", nativeQuery = true)
	public List<Fee> searchProjectsByThresholdUp(
			@Param("project") String project, 
			@Param("cc") int cc);
	
}
