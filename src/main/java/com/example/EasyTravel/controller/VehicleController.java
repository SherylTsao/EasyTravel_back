package com.example.EasyTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EasyTravel.service.ifs.VehicleService;
import com.example.EasyTravel.vo.VehicleRequest;
import com.example.EasyTravel.vo.VehicleResponse;

@CrossOrigin
@RestController
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@PostMapping(value = "add_car")
	public VehicleResponse addCar(@RequestBody VehicleRequest vehicleReq) {
		return vehicleService.addCar(vehicleReq.getLicensePlate(), vehicleReq.getCategory(), vehicleReq.getCc(),
				vehicleReq.getPrice());
	}

	@PostMapping(value = "update_car_info")
	public VehicleResponse updateCarInfo(@RequestBody VehicleRequest vehicleReq) {
		return vehicleService.updateCarInfo(vehicleReq.getLicensePlate(), vehicleReq.getOdo(),
				vehicleReq.getStatus());
	}

	@PostMapping(value = "scrap_car")
	public VehicleResponse scrapCar(@RequestBody VehicleRequest vehicleReq) {
		return vehicleService.scrapCar(vehicleReq.getLicensePlate());
	}

	@PostMapping(value = "find_car_by_category")
	public VehicleResponse findCarByCategory(@RequestBody VehicleRequest vehicleReq) {
		return vehicleService.findCarByCategory(vehicleReq.getCategory());
	}

	@GetMapping(value = "find_all_car")
	public VehicleResponse findAllCar() {
		return vehicleService.findAllCar();
	}

	@GetMapping(value = "find_car_near_scrap")
	public VehicleResponse findCarNearScrap() {
		return vehicleService.findCarNearScrap();
	}

	@GetMapping(value = "find_car_need_check")
	public VehicleResponse findCarNeedCheck() {
		return vehicleService.findCarNeedCheck();
	}

}
