package com.example.EasyTravel.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.VehicleService;
import com.example.EasyTravel.vo.VehicleResponse;

@Service
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private VehicleDao vehicleDao;

//	新增車輛方法
	@Override
	public VehicleResponse addCar(String licensePlate, String category, int cc, int price) {
//		判斷 > 車牌號碼
//		是否輸入車牌號碼
//		判斷 > 車輛類別 & cc引擎汽缸大小
		if (!StringUtils.hasText(licensePlate) || !StringUtils.hasText(category)) {
			return new VehicleResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
//		車輛是否存在
		Optional<Vehicle> op = vehicleDao.findById(licensePlate);
		if (op.isPresent()) {
			return new VehicleResponse(RtnCode.ALREADY_EXISTED.getMessage());
		}
		switch (category) {
		// bike > 0
		case "bike":
			if (cc != 0) {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
//			如果沒有break, 判斷false會一直進到下一個case
			break;
		// scooter > 1~250
		case "scooter":
			if (cc < 1 || cc >= 250) {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		// motorcycle > 251~550
		case "motorcycle":
			if (cc < 251 || cc >= 550) {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		// heavy motorcycle > 550~
		case "heavy motorcycle":
			if (cc < 550) {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		// sedan > 自定義(1200~4200)
		// ven, SUV > 自定義(2000~6600)
		case "sedan":
		case "ven":
		case "suv":
			if (cc <= 1200 || cc >= 6600) {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
//			若不符合以上case, 表輸入之車輛類別錯誤
		default:
			return new VehicleResponse(RtnCode.INCORRECT.getMessage());
		}

//		價格
		if (price < 1) {
			return new VehicleResponse(RtnCode.INCORRECT.getMessage());
		}

		Vehicle saveVehicle = new Vehicle(licensePlate, category, cc, price);
		return new VehicleResponse(vehicleDao.save(saveVehicle), RtnCode.SUCCESS.getMessage());
	}

//	修改資訊方法
	@Override
	public VehicleResponse updateCarInfo(String licensePlate, double odo, String status) {
//		判斷 > 車牌號碼
//		是否輸入車牌號碼
		if (!StringUtils.hasText(licensePlate)) {
			return new VehicleResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
//		車輛是否存在
		Optional<Vehicle> op = vehicleDao.findById(licensePlate);
		if (!op.isPresent()) {
			return new VehicleResponse(RtnCode.NOT_FOUND.getMessage());
		}

//		加總新總里程數
		if (odo < 0) {
			return new VehicleResponse(RtnCode.INCORRECT.getMessage());
		}
		double oldOdo = op.get().getOdo();
		double newOdo = 0.0;
		newOdo = oldOdo + odo;
		
		List<String> statusList = Arrays.asList("可租借", "租借中", "維護中", "調度中", "已報廢");
		if(!StringUtils.hasText(status)) {
			return new VehicleResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}else if(!statusList.contains(status)) {
			return new VehicleResponse(RtnCode.INCORRECT.getMessage());
		}

//		設定新vehicle接op內的東西 > 新可使用狀態&新總里程數蓋過舊資料 > 存進資料庫
		Vehicle updateVehicle = op.get();
		updateVehicle.updateVehicleEntity(status, newOdo);
		vehicleDao.save(updateVehicle);
		return new VehicleResponse(updateVehicle, RtnCode.SUCCESS.getMessage());
	}

//	報廢車輛
//	腳踏車7年、機車10年或12W公里、汽車15年或60W公里
	@Override
	public VehicleResponse scrapCar(String licensePlate) {
//		報廢車輛 狀態皆為 > 已報廢
		String status = "已報廢";
//		判斷 > 車牌號碼
//		是否輸入車牌號碼
		if (!StringUtils.hasText(licensePlate)) {
			return new VehicleResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
//		車輛是否存在
		Optional<Vehicle> op = vehicleDao.findById(licensePlate);
		if (!op.isPresent()) {
			return new VehicleResponse(RtnCode.NOT_FOUND.getMessage());
		}

//		與檢查日相比
		LocalDate today = LocalDate.now();
		LocalDate startServingDate = op.get().getStartServingDate();
//		判斷車種
		switch (op.get().getCategory()) {
		case "bike":
			if (today.getYear() - startServingDate.getYear() >= 7) {
//				可租用狀態改false
//				TODO
				op.get().setStatus(status);
			} else {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		case "scooter":
		case "motorcycle":
		case "heavy motorcycle":
			if (today.getYear() - startServingDate.getYear() >= 10 || op.get().getOdo() >= 120_000) {
				op.get().setStatus(status);
			} else {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		case "sedan":
		case "ven":
		case "suv":
			if (today.getYear() - startServingDate.getYear() >= 15 || op.get().getOdo() >= 600_000) {
				op.get().setStatus(status);
			} else {
				return new VehicleResponse(RtnCode.INCORRECT.getMessage());
			}
			break;
		default:
			return new VehicleResponse(RtnCode.NOT_FOUND.getMessage());
		}
		Vehicle scrapCar = op.get();
		vehicleDao.save(scrapCar);
		return new VehicleResponse(scrapCar, RtnCode.SUCCESS.getMessage());
	}

//	用車種找車
	@Override
	public VehicleResponse findCarByCategory(String category) {
//		檢查有無輸入車種
		if (!StringUtils.hasText(category)) {
			return new VehicleResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		List<Vehicle> carList = new ArrayList<Vehicle>();
		List<String> categoryList = Arrays.asList("bike", "scooter", "motorcycle", "heavy motorcycle", "sedan", "ven",
				"suv");
		if (!categoryList.contains(category)) {
			return new VehicleResponse(RtnCode.NOT_FOUND.getMessage());
		}
		carList.addAll(vehicleDao.findAllByCategory(category));
		return new VehicleResponse(carList, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public VehicleResponse findAllCar() {
		List<Vehicle> carInfo = new ArrayList<Vehicle>(vehicleDao.findAll());
		return new VehicleResponse(carInfo, RtnCode.SUCCESS.getMessage());
	}

	@Override
	public VehicleResponse findCarNearScrap() {
		return new VehicleResponse(vehicleDao.searchVehicleNearScrap(), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public VehicleResponse findCarNeedCheck() {
		return new VehicleResponse(vehicleDao.searchVehicleNeedCheck(), RtnCode.SUCCESS.getMessage());
	}

}
