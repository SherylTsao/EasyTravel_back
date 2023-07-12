package com.example.EasyTravel.constants;

public enum Abnormal {
	//依照車子狀況判斷是該維修還是檢查即可
	A01("A01", "エンジンがかからない"), 
	A02("A02", "車両に損傷があります!"), 
	A03("A03", " タイヤがパンクしています"),
	B01("B01", "車両のライトが異常です"), 
	B02("B02", " 車両の操作が異常です"),
	B03("B03", "エンジンの異音がします"),
	C01("C01", " ブレーキの摩耗があります。"),
	C02("C02", "ライトの異常があります"), 
	C03("C03", "点検日が過ぎています"), 
	C04("C04" ,"タイヤの摩耗が進んでいます"),
	C05("C05", "車体にキズがあります"),
	E("E", "処理中です");
	private String code;
	private String message;

	private Abnormal(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
