package com.example.EasyTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EasyTravel.service.ifs.StopService;
import com.example.EasyTravel.vo.StopRequest;
import com.example.EasyTravel.vo.StopResponse;

@CrossOrigin
@RestController
public class StopController {

	@Autowired
	private StopService stopService;

	@PostMapping(value = "add_stop")
	public StopResponse addStop(@RequestBody StopRequest request) {
		return stopService.addStop(request.getCity(), request.getLocation(), request.getBikeAmount(),
				request.getMotorcycleAmount(), request.getCarAmount());
	}
	
	@GetMapping(value = "show_all_stops")
	public StopResponse showAllStops() {
		return stopService.showAllStops();
	}

	@PostMapping(value = "find_city_stops")
	public StopResponse findCityStops(@RequestBody StopRequest request) {
		return stopService.findCityStops(request.getCity());
	}
	
	@PostMapping(value = "find_stops_vehicles")
	public StopResponse findStopsVehicles(@RequestBody StopRequest request) {
		return stopService.findStopsVehicles(request.getCity(), request.getLocation());
	}

	@PostMapping(value = "renew_amount")
	public StopResponse renewAmount(@RequestBody StopRequest request) {
		return stopService.renewAmount(request.getCity(), request.getLocation(), request.getBikeAmount(),
				request.getMotorcycleAmount(), request.getCarAmount());
	}

	@PostMapping(value = "rent_or_return")
	public StopResponse rentOrReturn(@RequestBody StopRequest request) {
		return stopService.rentOrReturn(request.isRent(), request.getCategory(), request.getCity(),
				request.getLocation());
	}

	@PostMapping(value = "dispatch")
	public StopResponse dispatch(@RequestBody StopRequest request) {
		return stopService.dispatch(request.getVehicleList(), request.getCity(), request.getLocation());
	}

}
