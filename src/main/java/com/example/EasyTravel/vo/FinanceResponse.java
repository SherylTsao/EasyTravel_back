package com.example.EasyTravel.vo;

import java.util.List;
import java.util.Map;

import com.example.EasyTravel.entity.Finance;

public class FinanceResponse {

	// 屬性
	private String message;
	private List<Finance> financeList;
	private Map<String, Object> financeMap;

	/*
	 * 建構方法
	 * 1.()
	 * 2.(message)
	 * 3.(financeList, message)
	 * 4.(financeMap, message)
	 */
	public FinanceResponse() {
		super();
	}

	public FinanceResponse(String message) {
		super();
		this.message = message;
	}

	public FinanceResponse(List<Finance> financeList, String message) {
		super();
		this.financeList = financeList;
		this.message = message;
	}

	public FinanceResponse(Map<String, Object> financeMap, String message) {
		super();
		this.financeMap = financeMap;
		this.message = message;
	}

	// getters & setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Finance> getFinanceList() {
		return financeList;
	}

	public void setFinanceList(List<Finance> financeList) {
		this.financeList = financeList;
	}

	public Map<String, Object> getFinanceMap() {
		return financeMap;
	}

	public void setFinanceMap(Map<String, Object> financeMap) {
		this.financeMap = financeMap;
	}

}
