package com.example.EasyTravel.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Finance;
import com.example.EasyTravel.repository.FinanceDao;
import com.example.EasyTravel.service.ifs.FinanceService;
import com.example.EasyTravel.vo.FinanceResponse;

@Service
public class FinanceServiceImpl implements FinanceService {

	// title符合範圍
	private String titleRegex = "vip_income|vehicle_cost|maintenance_cost|rent_income";
	// detail符合範圍
	private String detailRegex = "vip|bike|scooter|motorcycle|heavy_motorcycle|sedan|ven|suv|A0.|B0.|C0.";

	@Autowired
	private FinanceDao financeDao;

	/*
	 * 新增財務明細 
	 * 輸入參數: 
	 * -主項目(String):title 
	 * -次項目(String):detail 
	 * -金額(int):price
	 */
	@Override
	public FinanceResponse addReport(String title, String detail, int price) {
		/*
		 * 防止輸入空白或錯誤 
		 * 若'title'不在'titleRegex'內或'detail'不在'detailRegex'內或'price' < 0 :
		 * ->則回傳"訊息錯誤"
		 */
		if (!title.matches(titleRegex) || !detail.matches(detailRegex) || price < 0) {
			return new FinanceResponse(RtnCode.INCORRECT.getMessage());
		}
		/*
		 * 呼叫新增方法 "insertReport(title, detail, price, buildTime)"
		 * 	新增一不存在於資料庫的新資料
		 * ->若已存在則回傳"已存在" ->成功則回傳"成功"
		 */
		return financeDao.insertReport(title, detail, price, LocalDateTime.now()) == 0
				? new FinanceResponse(RtnCode.ALREADY_EXISTED.getMessage())
				: new FinanceResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 搜尋財務明細 
	 * 輸入參數: 
	 * -主項目(String):title 
	 * -月份(int):month
	 */
	@Override
	public FinanceResponse findTitle(String title, int month) {
		/*
		 * 查詢資料 
		 * 呼叫搜尋方法 "searchTitle(title, month)"
		 * 	查詢財務明細主項目為'title'、月份為'month'的所有資料
		 */
		List<Finance> resList = financeDao.searchTitle(title, month);
		/*
		 * 回傳資料 
		 * ->若'resList'內部無資料則回傳"找不到" 
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 */
		return resList.size() == 0 ? new FinanceResponse(RtnCode.NOT_FOUND.getMessage())
				: new FinanceResponse(resList, RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 搜尋財務明細 
	 * 輸入參數: 
	 * -次項目(String):detail 
	 * -月份(int):month
	 */
	@Override
	public FinanceResponse findDetail(String detail, int month) {
		/*
		 * 查詢資料 呼叫搜尋方法 "searchDetail(detail, month)"
		 * 	查詢財務明細次項目為'detail'、月份為'month'的所有資料
		 */
		List<Finance> resList = financeDao.searchDetail(detail, month);
		/*
		 * 回傳資料 
		 * ->若'resList'內部無資料則回傳"找不到" 
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 */
		return resList.size() == 0 ? new FinanceResponse(RtnCode.NOT_FOUND.getMessage())
				: new FinanceResponse(resList, RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 月財務報告 
	 * 輸入參數: 
	 * -月份(int):month
	 */
	@Override
	public FinanceResponse monthlyReport(int month) {
		/*
		 * 查詢資料 呼叫搜尋方法 "searchMonthData(month)"
		 * 	查詢財務明細月份為'month'的所有資料
		 */
		List<Finance> resList = financeDao.searchMonthData(month);
		/*
		 * 回傳資料 
		 * ->若'resList'內部無資料則回傳"找不到" 
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 * 	其中並對'resList'做資料處理-私有方法 "organize"
		 */
		return resList.size() == 0 ? new FinanceResponse(RtnCode.NOT_FOUND.getMessage())
				: new FinanceResponse(organize(resList), RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 月財務比例報告 
	 * 輸入參數: 
	 * -月份(int):month
	 */
	@Override
	public FinanceResponse monthlyRatio(int month) {
		/*
		 * 查詢資料 呼叫搜尋方法 "searchMonthData(month)"
		 * 	查詢財務明細月份為'month'的所有資料
		 */
		List<Finance> resList = financeDao.searchMonthData(month);
		/*
		 * 回傳資料 
		 * ->若'resList'內部無資料則回傳"找不到" 
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 * 	其中並對'resList'做資料處理-私有方法 "organizeRatio"
		 */
		return resList.size() == 0 ? new FinanceResponse(RtnCode.NOT_FOUND.getMessage())
				: new FinanceResponse(organizeRatio(resList), RtnCode.SUCCESS.getMessage());
	}
	
	/*
	 * 顯示所有財務明細 
	 * 輸入參數: 無
	 */
	@Override
	public FinanceResponse showAllReport() {
		/*
		 * 回傳資料 
		 * ->回傳"成功"並呼叫方法'findAll()'找尋所有資料
		 */
		return new FinanceResponse(financeDao.findAll(), RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 私有方法，資料整理 
	 * 帶入參數: 
	 * -財務明細資料(List<Finance>):list
	 */
	private Map<String, Object> organize(List<Finance> list) {
		/*
		 * 建立容器 
		 * 1.財務報告       (Map<String, Object>): financeMap 
		 * 2.交通工具財務分項 (Map<String, Double>): vehicleCostMap 
		 * 3.維護財務分項 	  (Map<String, Double>): maintenanceMap
		 * 4.租賃收入分項    (Map<String, Double>): rentMap
		 */
		Map<String, Object> financeMap = new HashMap<>();
		Map<String, Double> vehicleCostMap = new HashMap<>();
		Map<String, Double> maintenanceMap = new HashMap<>();
		Map<String, Double> rentMap = new HashMap<>();
		/*
		 * 將資料進行歸類整理，並分別置入容器中 
		 * 1.遍歷list 
		 * 2.並依照每個list的主項目'title'進行分類
		 * 將該筆明細的'detail'作為key，該筆明細的'price'作為value，放入'------Map'容器中，語意如同 =
		 * 		------Map.put([i].detail, ------Map([i].detail) += [i].price) 
		 * 	#若是該筆key在map中不存在，則新增並賦予value預設值為0，開始新增;若存在則將值進行累加 
		 * 結束後break
		 */
		for (Finance f : list) {
			switch (f.getTitle()) {
			// 當'title'為'vehicle_cost'時
			case "vehicle_cost":
				vehicleCostMap.put(f.getDetail(), vehicleCostMap.getOrDefault(f.getDetail(), 0.0) + f.getPrice());
				break;
			// 當'title'為'maintenance_cost'時
			case "maintenance_cost":
				maintenanceMap.put(f.getDetail(), maintenanceMap.getOrDefault(f.getDetail(), 0.0) + f.getPrice());
				break;
			// 當'title'為'rent_income'時
			case "rent_income":
				rentMap.put(f.getDetail(), rentMap.getOrDefault(f.getDetail(), 0.0) + f.getPrice());
				break;
			// 非以上時 
			default:
				financeMap.put(f.getTitle(), (double) financeMap.getOrDefault(f.getTitle(), 0.0) + f.getPrice());
			}
		}
		/*
		 * 完成所有明細分類，將各分項加入'financeMap'的容器中 
		 * 並個別賦予key值: "vehicle_cost": vehicleCostMap
		 * 				 "maintenance_cost": maintenanceMap 
		 * 				 "rent_income": rentMap 
		 * 最後回傳 'financeMap'
		 */
		financeMap.put("vehicle_cost", vehicleCostMap);
		financeMap.put("maintenance_cost", maintenanceMap);
		financeMap.put("rent_income", rentMap);
		return financeMap;
	}

	/*
	 * 私有方法，資料整理並以比例形式輸出 
	 * 帶入參數: 
	 * -財務明細資料(List<Finance>):list
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> organizeRatio(List<Finance> list) {
		// 呼叫私有方法 "organize(list)"，進行財務明細資料整理 
		Map<String, Object> financeMap = organize(list);
		/*
		 * 取出財務報告的所有value值進行加總，並依照key值分別進行操作 
		 * 當遇到value值為Map物件時，需轉成Map物件，再對物件中的各項value值進行加總，語意如下 =
		 * 	轉成Map物件 => (Map<String, Double>) financeMap.get("   ") 
		 * 	進行加總方法 => map.values().stream().reduce(0.0, Double::sum) 
		 * 補充說明:
		 * reduce()函数会将初始值和Stream中的元素依次进行求和操作，最终得到加总结果
		 * Double::sum 引用了Double類的sum方法。這個方法接收兩個double值並返回它們的和
		 */
		// key值為'vehicle_cost'
		final double VehicleExpense = ((Map<String, Double>) financeMap.get("vehicle_cost"))
				.values().stream().reduce(0.0, Double::sum);
		// key值為'maintenance_cost'
		final double MaintenanceExpense = ((Map<String, Double>) financeMap.get("maintenance_cost"))
				.values().stream().reduce(0.0, Double::sum);
		// key值為'rent_income'
		final double RentIncome = ((Map<String, Double>) financeMap.get("rent_income"))
				.values().stream().reduce(0.0, Double::sum);
		// key值為'vip_income'
		final double VipIncome = (double) financeMap.get("vip_income");
		// 支出與收入分別加總
		final double TotalExpense = VehicleExpense + MaintenanceExpense;
		final double TotalIncome = VipIncome + RentIncome;
		/*
		 * 將財務報告中的所有value值取代成百分比，並依照分項做百分比轉換 
		 * 運用lambda表達式及使用Map.replaceAll方法來處理financeMap中的每個鍵值
		 * 對於每個鍵值對，我們根據鍵的不同進行相應的處理，並將新的值返回給replaceAll方法
		 */
		financeMap.replaceAll((key, value) -> {
		    switch (key) {
		    /*
		     * 將value為Map物件的值轉換為Map<String, Double>物件型態
		     * 使用.entrySet()取得這個Map物件的鍵值對集合（Set<Map.Entry<String, Double>>）
		     * 使用.stream()將鍵值對集合轉換為串流（Stream<Map.Entry<String, Double>>）
		     * 使用.collect(Collectors.toMap(...))將串流轉換為一個新的Map物件
		     * 在.toMap(...)方法中，我們需要提供兩個參數：
		     * 	第一個參數Entry::getKey表示使用鍵（key）作為新Map的鍵
		     * 	第二個參數entry -> 呼叫私有方法"toPercent()"表示使用對應的值（value）進行計算，並將計算結果作為新Map的值
		     * 最後，這個新的Map物件就被return返回，取代原本
		     */
		    	// key值為'vehicle_cost'
		        case "vehicle_cost":
		            return ((Map<String, Double>) value).entrySet().stream()
		                .collect(Collectors.toMap(Entry::getKey, entry -> toPercent(entry.getValue(), VehicleExpense)
		                ));
		        // key值為'maintenance_cost'
		        case "maintenance_cost":
		            return ((Map<String, Double>) value).entrySet().stream()
		                .collect(Collectors.toMap(Entry::getKey, entry -> toPercent(entry.getValue(), MaintenanceExpense)
		                ));
		        // key值為'rent_income'
		        case "rent_income":
		            return ((Map<String, Double>) value).entrySet().stream()
		                .collect(Collectors.toMap(Entry::getKey, entry -> toPercent(entry.getValue(), RentIncome)
		                ));
		        // key值為'vip_income'
		        case "vip_income":
		            return toPercent((double) value, TotalIncome);
		    }
			return key;
		});
		/*
		 * 另儲存分項百分比資訊及總收入支出資訊
		 * 最後回傳結果
		 */
		financeMap.put("vehicle_cost_ratio", toPercent(VehicleExpense, TotalExpense));
		financeMap.put("maintenance_cost_ratio", toPercent(MaintenanceExpense, TotalExpense));
		financeMap.put("rent_income_ratio", toPercent(RentIncome, TotalIncome));
		financeMap.put("total_income", TotalIncome);
		financeMap.put("total_expense", TotalExpense);
		return financeMap;
	}
	
	/*
	 * 私有方法，轉換成百分比
	 * 帶入參數: 
	 * -分子(double):numerator
	 * -分母(double):denominator
	 */
	private double toPercent(double numerator,double denominator) {
		/*
		 * 回傳計算結果
		 * 計算後取到小數點後一位
		 */
		return (Math.round((numerator / denominator * 1000.0)) / 10.0);
	}

}
