package com.example.EasyTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EasyTravel.service.ifs.FeeService;
import com.example.EasyTravel.vo.FeeRequest;
import com.example.EasyTravel.vo.FeeResponse;

@CrossOrigin
@RestController
public class FeeController {

	@Autowired
	private FeeService feeService;

	@PostMapping(value = "add_project")
	public FeeResponse addProject(@RequestBody FeeRequest request) {
		return feeService.addProject(request.getProject(), request.getCc(), request.getRate(), request.getThreshold());
	}

	@DeleteMapping(value = "delete_project")
	public FeeResponse deleteProject(@RequestBody FeeRequest request) {
		return feeService.deleteProject(request.getProject(), request.getCc(), request.getRate());
	}

	@PostMapping(value = "find_projects")
	public FeeResponse findProjects(@RequestBody FeeRequest request) {
		return feeService.findProjects(request.getProject(), request.getCc());
	}

	@PostMapping(value = "calculate")
	public FeeResponse calculate(@RequestBody FeeRequest request) {
		return feeService.calculate(request.getVehicle(), request.isVip(), request.getPeriod());
	}
	
	@GetMapping(value = "show_all_fees")
	public FeeResponse showAllFees() {
		return feeService.showAllFees();
	}
}
