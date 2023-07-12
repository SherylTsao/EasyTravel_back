package com.example.EasyTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.EasyTravel.service.ifs.FinanceService;
import com.example.EasyTravel.vo.FinanceRequest;
import com.example.EasyTravel.vo.FinanceResponse;

@CrossOrigin
@RestController
public class FinanceController {

	@Autowired
	private FinanceService financeService;

	@PostMapping(value = "add_report")
	public FinanceResponse addReport(@RequestBody FinanceRequest request) {
		return financeService.addReport(request.getTitle(), request.getDetail(), request.getPrice());
	}

	@PostMapping(value = "find_title")
	public FinanceResponse findTitle(@RequestBody FinanceRequest request) {
		return financeService.findTitle(request.getTitle(), request.getMonth());
	}

	@PostMapping(value = "find_detail")
	public FinanceResponse findDetail(@RequestBody FinanceRequest request) {
		return financeService.findDetail(request.getDetail(), request.getMonth());
	}

	@PostMapping(value = "monthly_report")
	public FinanceResponse monthlyReport(@RequestBody FinanceRequest request) {
		return financeService.monthlyReport(request.getMonth());
	}

	@PostMapping(value = "monthly_ratio")
	public FinanceResponse monthlyRatio(@RequestBody FinanceRequest request) {
		return financeService.monthlyRatio(request.getMonth());
	}
	
	@GetMapping(value = "show_all_report")
	public FinanceResponse showAllReport() {
		return financeService.showAllReport();
	}

}
