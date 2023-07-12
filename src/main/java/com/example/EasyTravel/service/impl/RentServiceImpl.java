package com.example.EasyTravel.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Rent;
import com.example.EasyTravel.entity.UserInfo;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.RentDao;
import com.example.EasyTravel.repository.UserInfoDao;
import com.example.EasyTravel.repository.VehicleDao;
import com.example.EasyTravel.service.ifs.FeeService;
import com.example.EasyTravel.service.ifs.FinanceService;
import com.example.EasyTravel.service.ifs.RentService;
import com.example.EasyTravel.service.ifs.StopService;
import com.example.EasyTravel.vo.FeeResponse;
import com.example.EasyTravel.vo.RentResponse;
import com.example.EasyTravel.vo.StopResponse;

@Service
public class RentServiceImpl implements RentService {

	@Autowired
	private VehicleDao vehicleDao;

	@Autowired
	private UserInfoDao userInfoDao;

	@Autowired
	private StopService stopService;

	@Autowired
	private FeeService feeService;

	@Autowired
	private FinanceService financeService;

	@Autowired
	private RentDao rentDao;

	/*
	 * 租車
	 * 輸入參數:
	 * -帳號(String):account
	 * -車牌(String):licensePlate
	 */
	@Override
	public RentResponse rent(String account, String licensePlate) {
		/*
		 * 確認"帳號"狀態與"車"存在
		 * 帳號部分，呼叫自定義方法"checkAccount(account)"，找尋帳號及其駕照、vip等資格，未啟用帳號將會排除
		 * 若'user'為空(null) 或 'vehicle'不存在
		 * ->則回傳"未找到"
		 */
		UserInfo user = userInfoDao.checkAccount(account);
		Optional<Vehicle> vehicle = vehicleDao.findById(licensePlate);
		if (user == null || !vehicle.isPresent()) {
			return new RentResponse(RtnCode.NOT_FOUND.getMessage());
		}
		/*
		 * 租借資格審核
		 * 如果:
		 *  1.租借車輛為"機車"種類，但沒有駕照狀況 或 
		 *  2."汽車"種類但沒有駕照狀況 或 
		 *  3.車輛目前為不可租用狀態
		 * ->則回傳"不正確"
		 */
		if ((vehicle.get().getCategory().matches("scooter|motorcycle|heavy motorcycle") && !user.isMotorcycleLicense())
				|| (vehicle.get().getCategory().matches("sedan|ven|suv") && !user.isDrivingLicense())
				|| !vehicle.get().getStatus().equals("可租借")) {
			return new RentResponse(RtnCode.INCORRECT.getMessage());
		}
		/*
		 * 租車-更新站點狀態
		 * 若方法"rentOrReturn(true, category, city, location)"，回傳非"成功"結果
		 * ->則回傳此方法的錯誤信息
		 */
		StopResponse response = stopService.rentOrReturn(true, vehicle.get().getCategory(), vehicle.get().getCity(),
				vehicle.get().getLocation());
		if (!response.getMessage().equals(RtnCode.SUCCESSFUL.getMessage())) {
			return new RentResponse(response.getMessage());
		}
		/*
		 * 更新狀態並建立明細回傳結果
		 * 1.更新交通工具狀態
		 * 	呼叫自定義方法"updateRentInfo(licensePlate, false, null, null, 0)"，將可租借狀態切換成false，並將當前站點改成null
		 * 2.建立一則租借明細"租"
		 * 	儲存一新建檔案，包含當前時間的明細及其他資訊
		 * ->最後回傳"成功"
		 */
		vehicleDao.updateRentInfo(licensePlate, "租借中", null, null, 0);
		rentDao.save(new Rent(account, licensePlate, vehicle.get().getCity(), vehicle.get().getLocation()));
		return new RentResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 還車
	 * 輸入參數:
	 * -帳號(String):account
	 * -車牌(String):licensePlate
	 * -城市(String):city
	 * -站點(String):location
	 * -里程(double):odo
	 */
	@Override
	public RentResponse dropOff(String account, String licensePlate, String city, String location, double odo) {
		/*
		 * 確認是否有租車資訊 與 帳號狀態 與 交通工具存在
		 * 帳號部分，呼叫自定義方法"checkAccount(account)"，找尋帳號及其駕照、vip等資格
		 * 租車部分，呼叫自定義方法"findTop1ByAccountAndLicensePlateOrderBySerialNumberDesc(account, licensePlate)"，找最新租借單號
		 */
		UserInfo user = userInfoDao.checkAccount(account);
		Optional<Vehicle> vehicle = vehicleDao.findById(licensePlate);
		Rent rent = rentDao.findTop1ByAccountOrderBySerialNumberDesc(account);
		/*
		 * 若'user'為空(null) 或 'vehicle'不存在 或 'rent'不存在 或 'rent'最新單非租車
		 * ->則回傳"未找到"
		 */ 
		if (user == null || !vehicle.isPresent() || rent == null || rent.isRent() == false) {
			return new RentResponse(RtnCode.NOT_FOUND.getMessage());
		}
		/*
		 * 取出租車時間點並記錄當下時間點，依照車種進行經費計算
		 * 呼叫方法"calculate(vehicle, isVip,	Duration.between(startTime, endTime))，進行租借經費計算
		 */
		FeeResponse calRes = feeService.calculate(vehicle.get(), user.isVip(),
				Duration.between(rent.getNowTime(), LocalDateTime.now()));
		/*
		 * 若'calRes'為非"成功"結果
		 * ->則回傳此方法的錯誤信息
		 */
		if (!calRes.getMessage().equals(RtnCode.SUCCESS.getMessage())) {
			return new RentResponse(calRes.getMessage());
		}
		/*
		 * 還車-更新站點狀態
		 * 若方法"rentOrReturn(false, category, city, location)"，回傳非"成功"結果
		 * ->則回傳此方法的錯誤信息
		 */
		StopResponse response = stopService.rentOrReturn(false, vehicle.get().getCategory(), city, location);
		if (!response.getMessage().equals(RtnCode.SUCCESSFUL.getMessage())) {
			return new RentResponse(response.getMessage());
		}
		/*
		 * 更新狀態儲存並建立還車明細
		 * 1.還車-更新交通工具狀態
		 * 	呼叫自定義方法"updateRentInfo(licensePlate, true, city, location, odo)"，將可租借狀態切換成true，並存入當前站點資訊
		 * 2.建立一則租借明細"租"
		 * 	儲存一新建檔案，包含當前時間的明細及其他資訊，同時包含租借經費的總額
		 * 3.建立一財務明細
		 *  建立一以"rent_income"為'title'的明細，並附上'detail'、'price'等，其中包含附上當前時間的資訊
		 * ->最後回傳"成功"
		 */
		vehicleDao.updateRentInfo(licensePlate, "可租借", city, location, odo > 0 ? odo : 0);
		rentDao.save(new Rent(account, licensePlate, city, location, false, calRes.getTotal()));
		financeService.addReport("rent_income", vehicle.get().getCategory(), calRes.getTotal());
		return new RentResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 顯示個人租借明細
	 * 輸入參數:
	 * -帳號(String):account
	 */
	@Override
	public RentResponse showDetails(String account) {
		/*
		 * 呼叫自定義方法"findByAccount(account)"，尋找同帳號的所有租借明細
		 * 若'res'內部無內容
		 * ->則回傳"未找到"
		 * 若有資料
		 * ->則回傳"成功"並附帶資料陣列
		 */
		List<Rent> res = rentDao.findByAccount(account);
		return res.size() == 0 ? new RentResponse(RtnCode.NOT_FOUND.getMessage())
				: new RentResponse(res, RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 月租人次計算
	 * 輸入參數:
	 * -月份(int):month
	 */
	@SuppressWarnings("serial")
	@Override
	public RentResponse monlyRentalCount(int month) {
		/*
		 * 取出目標月份所有資料並計算各租借人次
		 * 若'res'內部無資料
		 * ->則回傳"未找到"
		 */
		List<Rent> res = rentDao.searchMonthData(month);
		if (res == null) {
			return new RentResponse(RtnCode.NOT_FOUND.getMessage());
		}
		/*
		 * 進行統計分類
		 * 建立一容器'statics'
		 * 紀錄每個車牌的租借次數
		 */
		Map<String, Integer> statics = new HashMap<>();
		res.forEach(r -> statics.put(r.getLicensePlate(), statics.getOrDefault(r.getLicensePlate(), 0) + 1));
		/*
		 * 找出車牌的對應車種
		 * 呼叫自定義方法"searchVehicleCategory(licensePlateList)"，尋找各車牌的種類
		 * 並建立一容器Map<String, Integer>，統計儲存各車種的借用次數
		 */
		List<Vehicle> vehicleCategories = vehicleDao.searchVehicleCategory(new ArrayList<>(statics.keySet()));
		Map<String, Integer> eachTimes = new HashMap<>() {
			{
				/*
				 * 1.首先將vehicleCategories轉換成一個流(Stream)物件，以便對其元素進行操作。
				 * 2.使用filter方法，篩選出符合指定條件的元素。
				 *  filter(v -> v.getCategory().matches("sedan|ven|suv"))，語意如下:
				 * 	取得每個元素的'category'，並使用matches方法檢查是否匹配目標字串，只有符合條件的元素才會被保留下來。
				 * 3.使用mapToInt方法將元素轉換為整數，以便進行統計計算。
				 *  mapToInt(v -> statics.get(v.getLicensePlate()))，語意如下：
				 *  使用getLicensePlate()方法獲取每個元素的車牌號碼，並通過statics物件的get方法獲取對應的統計數值。
				 * 4.最後使用sum方法計算所有元素的總和。
				 * 返回一個整數，代表符合條件的元素的統計數值的總和，同時建立鍵值對至容器'eachTimes'中。
				 */
				put("bikeTimes", vehicleCategories.stream()
					    .filter(v -> v.getCategory().equals("bike"))
					    .mapToInt(v -> statics.get(v.getLicensePlate()))
					    .sum());
				put("motorcycleTimes", vehicleCategories.stream()
					    .filter(v -> v.getCategory().matches("scooter|motorcycle|heavy motorcycle"))
					    .mapToInt(v -> statics.get(v.getLicensePlate()))
					    .sum());
				put("carTimes", vehicleCategories.stream()
					    .filter(v -> v.getCategory().matches("sedan|ven|suv"))
					    .mapToInt(v -> statics.get(v.getLicensePlate()))
					    .sum());
			}
		};
		/*
		 * ->回傳"成功"並附帶此一容器結果
		 */
		return new RentResponse(eachTimes, RtnCode.SUCCESS.getMessage());
	}

}
