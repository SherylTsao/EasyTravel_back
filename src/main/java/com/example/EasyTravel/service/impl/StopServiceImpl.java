package com.example.EasyTravel.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Stop;
import com.example.EasyTravel.entity.StopId;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.StopDao;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.StopService;
import com.example.EasyTravel.vo.StopResponse;
import com.example.EasyTravel.vo.VehicleCount;

@Service
public class StopServiceImpl implements StopService {

	@Autowired
	private StopDao stopDao;

	@Autowired
	private VehicleDao vehicleDao;

	/*
	 * 新增站點 
	 * 輸入參數: 
	 * -城市(String):city 
	 * -站點(String):location 
	 * -腳踏車存量(int):bikeAmount
	 * -機車存量(int):motorcycleAmount 
	 * -汽車存量(int):carAmount
	 */
	@Override
	public StopResponse addStop(String city, String location, int bikeAmount, int motorcycleAmount, int carAmount) {
		/*
		 * 防空輸入 
		 * 若'city'為空(null或"") 或 'location'為空(null或"") 或 'bikeAmount' < 0 或 'motorcycleAmount' <0 或 'carAmount' < 0 
		 * ->則回傳"不得為空"
		 */
		if (!StringUtils.hasText(city) || !StringUtils.hasText(location) || bikeAmount < 0 || motorcycleAmount < 0
				|| carAmount < 0) {
			return new StopResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		/*
		 * 呼叫新增方法 "insertStop(city, location, bikeAmount, motorcycleAmount, carAmount)" 新增一不存在於資料庫的新資料 
		 * ->若已存在則回傳"已存在" 
		 * ->成功則回傳"成功"
		 */
		return stopDao.insertStop(city, location, bikeAmount, motorcycleAmount, carAmount) == 0
				? new StopResponse(RtnCode.ALREADY_EXISTED.getMessage())
				: new StopResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 顯示全部站點
	 */
	@Override
	public StopResponse showAllStops() {
		/*
		 * 回傳"成功"並包含資料陣列，由方法"findAll()"所找出的所有資料
		 */
		return new StopResponse(stopDao.findAll(), RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 尋找站點 
	 * 輸入參數: 
	 * -城市(String):city
	 */
	@Override
	public StopResponse findCityStops(String city) {
		/*
		 * 查詢資料 
		 * 呼叫搜尋方法 "searchStops(city)" 查詢城市為'city'的所有資料
		 */
		List<Stop> resList = stopDao.searchStops(city);
		/*
		 * 回傳資料 
		 * ->若'resList'內部無資料則回傳"找不到" 
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 */
		return resList.size() == 0 ? new StopResponse(RtnCode.NOT_FOUND.getMessage())
				: new StopResponse(resList, RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 尋找站點 
	 * 輸入參數: 
	 * -城市(String):city 
	 * -站點(String):location
	 */
	@Override
	public StopResponse findStopsVehicles(String city, String location) {
		/*
		 * 查詢資料 
		 * 呼叫搜尋方法 "findById(new StopId(city, location))" 查詢城市為'city'及'location'的資料
		 */
		Optional<Stop> res = stopDao.findById(new StopId(city, location));
		/*
		 * 回傳資料 
		 * ->若'res'存在則回傳"成功"
		 * 	並含此res站點的資訊及呼叫搜尋方法 "searchVehiclesByCityLocation(city, location)" 在此站點的所有交通工具資訊
		 * ->否則回傳"找不到"
		 */
		return res.isPresent()
				? new StopResponse(res.get(), vehicleDao.searchVehiclesByCityLocation(city, location),
						RtnCode.SUCCESS.getMessage())
				: new StopResponse(RtnCode.NOT_FOUND.getMessage());
	}

	/*
	 * 更新站點交通工具存量 
	 * 輸入參數: 
	 * -城市(String):city 
	 * -站點(String):location
	 * -腳踏車存量(int):bikeAmount
	 * -機車存量(int):motorcycleAmount 
	 * -汽車存量(int):carAmount
	 */
	@Override
	public StopResponse renewAmount(String city, String location, int bikeAmount, int motorcycleAmount, int carAmount) {
		/*
		 * 查詢資料 
		 * 呼叫搜尋方法 "findById(new StopId(city, location))" 查詢城市為'city'及'location'的資料
		 */
		Optional<Stop> res = stopDao.findById(new StopId(city, location));
		/*
		 * 判斷輸入並更改數量
		 * 以下使用了 res.map() 方法，對於 res 這個集合中的每個元素（stop），都要執行一個操作。操作的定義在箭頭函式 stop -> {...} 中
		 * 其中首先建立'flag'，並賦予初值false，以判別是否有進行數值更新的操作
		 */
		return res.map(stop -> {
			boolean flag = false;
			/*
			 * 更新腳踏車數量，對於數量的判斷:
			 * 	如果'bikeAmount'是負數 且 'stop'的腳踏車數量>='bikeAmount'的絕對值
			 * 	或者'bikeAmount'是正數，
			 * 則更新'stop'的腳踏車數量，並將'flag'設為true。
			 */
			if ((bikeAmount < 0 & stop.getBikeAmount() >= Math.abs(bikeAmount)) || bikeAmount > 0) {
				stop.setBikeAmount(stop.getBikeAmount() + bikeAmount);
				flag = true;
			}
			/*
			 * 更新機車數量，對於數量的判斷:
			 * 	如果'motorcycleAmount'是負數 且 'stop'的機車數量>='motorcycleAmount'的絕對值
			 * 	或者'motorcycleAmount'是正數，
			 * 則更新'stop'的機車數量，並將'flag'設為true。
			 */
			if ((motorcycleAmount < 0 & stop.getMotorcycleAmount() >= Math.abs(motorcycleAmount))
					|| motorcycleAmount > 0) {
				stop.setMotorcycleAmount(stop.getMotorcycleAmount() + motorcycleAmount);
				flag = true;
			}
			/*
			 * 更新汽車數量，對於數量的判斷:
			 * 	如果'carAmount'是負數 且 'stop'的汽車數量>='carAmount'的絕對值
			 * 	或者'carAmount'是正數，
			 * 則更新'stop'的汽車數量，並將'flag'設為true。
			 */
			if ((carAmount < 0 & stop.getCarAmount() >= Math.abs(carAmount)) || carAmount > 0) {
				stop.setCarAmount(stop.getCarAmount() + carAmount);
				flag = true;
			}
			/*
			 * 只要成功更新到其中一項目，便會回傳資料 
			 * ->若'flag'為true則回傳"成功"，並呼叫方法"save(stop)"，儲存站點的資訊並回傳此資訊
			 * ->否則回傳"不正確"
			 */
			return flag ? new StopResponse(stopDao.save(stop), RtnCode.SUCCESS.getMessage())
					: new StopResponse(RtnCode.INCORRECT.getMessage());
			/*
			 * 若未進行上述操作，亦即res不存在
			 * ->則回傳"未找到"
			 */
		}).orElse(new StopResponse(RtnCode.NOT_FOUND.getMessage()));
	}

	/*
	 * 站點租還資訊更新
	 * 輸入參數: 
	 * -是否租還(boolean):isRent
	 * -交通工具種類(String):category 
	 * -城市(String):city 
	 * -站點(String):location
	 */
	@SuppressWarnings("serial")
	@Override
	public StopResponse rentOrReturn(boolean isRent, String category, String city, String location) {
		/*
		 * 確認交通工具種類及是否租借
		 * 以'type'(int)建立租借類型，十位數是租借種類，個位數為是否租借
		 * 由'category'及'isRent'兩輸入參數進行判斷並分類
		 */
		int type = 0;
		switch (StringUtils.hasText(category) ? category : "") {
		/*
		 * 腳踏車為一類
		 */
		case "bike":
			type = isRent ? 10 : 11;
			break;
		/*
		 * 機車有三類
		 * "scooter"、"motorcycle"、"heavy motorcycle"
		 */
		case "scooter":
		case "motorcycle":
		case "heavy motorcycle":
			type = isRent ? 20 : 21;
			break;
		/*
		 * 汽車有三類
		 * "sedan"、"ven"、"suv"
		 */
		case "sedan":
		case "ven":
		case "suv":
			type = isRent ? 30 : 31;
			break;
		default:
		    /*
			 * 若'category'輸入不存在
			 * ->則回傳"未找到"
			 */
			return new StopResponse(RtnCode.NOT_FOUND.getMessage());
		}
		/*
		 * 建立租借對照表Map<Integer, BiFunction<String, String, Integer>>
		 * 將自定義方法，以BiFunction形式，存入Map中，其中key值為方才所建立的type，自定義方法如下:
		 * key:10, value:stopDao.minusBike(city, location)
		 * key:11, value:stopDao.plusBike(city, location)
		 * key:20, value:stopDao.minusMotorcycle(city, location)
		 * key:21, value:stopDao.plusMotorcycle(city, location)
		 * key:30, value:stopDao.minusCar(city, location)
		 * key:31, value:stopDao.plusCar(city, location)
		 */
		Map<Integer, BiFunction<String, String, Integer>> operationMap = new HashMap<>() {
			{
				put(10, (inputC, inputL) -> stopDao.minusBike(city, location));
				put(11, (inputC, inputL) -> stopDao.plusBike(city, location));
				put(20, (inputC, inputL) -> stopDao.minusMotorcycle(city, location));
				put(21, (inputC, inputL) -> stopDao.plusMotorcycle(city, location));
				put(30, (inputC, inputL) -> stopDao.minusCar(city, location));
				put(31, (inputC, inputL) -> stopDao.plusCar(city, location));
			}
		};
		/*
		 * 帶入輸入參數執行程式，並返回租還结果
		 * 由對照表中取得操作類型（type）對應的函数。如果對照表中不存在此類型的函数，则返回一個接受两個String型態輸入變數並返回0的預設函数
		 * "apply(city, location)"表示對取得函数進行操作，輸入'city'和'location'，并返回操作的结果
		 * ->若反為參數為0，代表執行失敗，回傳"不正確"
		 * ->若成功則返回"成功"
		 */
		return operationMap.getOrDefault(type, (inputC, inputL) -> 0).apply(city, location) == 0
				? new StopResponse(RtnCode.INCORRECT.getMessage())
				: new StopResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 車輛調度
	 * 輸入參數: 
	 * -交通工具車牌(List<String>):vehicleList
	 * -城市(String):city 
	 * -站點(String):location
	 */
	@Override
	public StopResponse dispatch(List<String> vehicleList, String city, String location) {
		/*
		 * 呼叫方法"sortCategory(vehicleList)"，對交通工具車牌串列進行分類，並回傳種類及數量
		 * 若回傳的交通工具總數量與原陣列不相符
		 * ->則回傳"不正確
		 * 補充:sortRes.stream().mapToLong(c -> c.getCount()).sum() 此程式為取出陣列內的'count'屬性並加總
		 * sortRes為空字串亦不會出錯，只會回傳0，因此可避免程式中斷
		 */
		List<VehicleCount> sortRes = vehicleDao.sortCategory(vehicleList);
		if (sortRes.stream().mapToLong(c -> c.getCount()).sum() != vehicleList.size()) {
			return new StopResponse(RtnCode.INCORRECT.getMessage());
		}
		/*
		 * 對^各別^交通工具變更站點，同時要對站點存量進行修正
		 * 因前面已確定交通工具存在，故此處方法"findAllById(vehicleList)"，可不用進行防呆檢查
		 * 此處使用lambda方式進行遍歷修改，並呼叫方法"rentOrReturn(true, v.getCategory(), v.getCity(), v.getLocation())"
		 */
		List<Vehicle> vehicles = vehicleDao.findAllById(vehicleList);
		vehicles.forEach(v -> rentOrReturn(true, v.getCategory(), v.getCity(), v.getLocation()));
		/*
		 * 紀錄各交通工具種類數量
		 * 建立三種容器各別裝不同種類的交通工具總數量
		 * 1.'bikeCount' += ('category' == "bike") ? getCount : 0
		 * 2.'motorcycleCount' += ('category' == ("scooter" or "motorcycle" or "heavy motorcycle")) ? getCount : 0
		 * 3.'carCount' += ('category' == ("sedan" or "ven" or "suv")) ? getCount : 0
		 * 最後進行新站點數量更新，呼叫方法"renewLimit(city, location, bikeCount, motorcycleCount, carCount)"
		 */
		int bikeCount = 0, motorcycleCount = 0, carCount = 0;
		for (VehicleCount s : sortRes) {
			bikeCount += s.getCategory().equals("bike") ? (int) s.getCount() : 0;
			motorcycleCount += s.getCategory().matches("scooter|motorcycle|heavy motorcycle") ? (int) s.getCount() : 0;
			carCount += s.getCategory().matches("sedan|ven|suv") ? (int) s.getCount() : 0;
		}
		renewAmount(city, location, bikeCount, motorcycleCount, carCount);
		/*
		 * 更新車輛調度新站點
		 * 若站點更新完成數量與車牌陣列數目不一致
		 * ->回傳"未找到"
		 * ->相等則回傳"成功"
		 */
		return vehicleDao.dispatch(vehicleList, city, location) != vehicleList.size()
				? new StopResponse(RtnCode.NOT_FOUND.getMessage())
				: new StopResponse(RtnCode.SUCCESSFUL.getMessage());
	}

}
