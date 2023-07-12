package com.example.EasyTravel.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.EasyTravel.constants.RtnCode;
import com.example.EasyTravel.entity.Fee;
import com.example.EasyTravel.entity.Vehicle;
import com.example.EasyTravel.repository.FeeDao;
import com.example.EasyTravel.service.ifs.FeeService;
import com.example.EasyTravel.vo.FeeResponse;

@Service
public class FeeServiceImpl implements FeeService {

	@Autowired
	private FeeDao feeDao;

	/*
	 * 新增費率方案
	 * 輸入參數:
	 * -方案名稱(String):project
	 * -交通工具排氣數(int):cc
	 * -費率(double):rate
	 * -時間閾值(int):threshold
	 */
	@Override
	public FeeResponse addProject(String project, int cc, double rate, int threshold) {
		/*
		 * 防止輸入空白或錯誤 
		 * 若'project'為空(null或"") 或'cc'、'rate'、'threshold'小於0:
		 * ->則回傳"不得為空"
		 */
		if (!StringUtils.hasText(project) || cc < 0 || rate < 0 || threshold < 0) {
			return new FeeResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		/*
		 * 呼叫新增方法 "insertProject(project, cc, rate, threshold)"，新增一不存在於資料庫的新資料
		 * ->若已存在則回傳"已存在"
		 * ->成功則回傳"成功"
		 */
		return feeDao.insertProject(project, cc, rate, threshold) == 1
				? new FeeResponse(RtnCode.SUCCESSFUL.getMessage())
				: new FeeResponse(RtnCode.ALREADY_EXISTED.getMessage());
	}

	/*
	 * 刪除費率方案
	 * 輸入參數:
	 * -方案名稱(String):project
	 * -交通工具排氣數(Integer):cc !可null
	 * -費率(Double):rate !可null
	 */
	@Override
	public FeeResponse deleteProject(String project, Integer cc, Double rate) {
		/*
		 * 刪除費率
		 * 宣告"旗子"，確認是否完成刪除動作
		 * 雙三元式包覆，邏輯判斷如下
		 * 若'rate'及'cc'同時為null:
		 * 	呼叫刪除方法 "deleteProjects(project)"，刪除方案名稱為'project'的所有資料
		 * 若'cc'不為null且大於0:
		 * 	呼叫刪除方法 "deleteProjects(project, cc)"，刪除方案名稱為'project'及排氣量為'cc'的所有資料
		 * 若以上皆非:
		 * 	呼叫刪除方法 "deleteProject(project, cc, rate)"，刪除方案名稱為'project'、排氣量為'cc'及費率為'rate'的資料
		 */
		int flag = (rate == null && cc == null) ? feeDao.deleteProjects(project)
				: (cc > 0) ? feeDao.deleteProjects(project, cc) : feeDao.deleteProject(project, cc, rate);
		/*
		 * 刪除方案
		 * ->若刪除失敗(flag == 0)，則回傳"不正確"
		 * ->成功則回傳"成功"
		 */
		return flag == 0 ? new FeeResponse(RtnCode.INCORRECT.getMessage())
				: new FeeResponse(RtnCode.SUCCESSFUL.getMessage());
	}

	/*
	 * 尋找費率方案
	 * 輸入參數:
	 * -方案名稱(String):project
	 * -交通工具排氣數(Integer):cc !可null
	 */
	@Override
	public FeeResponse findProjects(String project, Integer cc) {
		/*
		 * 查找資料
		 * 雙三元式包覆，邏輯判斷如下
		 * 若'project'內容為空(null或""):
		 * 	則回傳null
		 * 若'cc'為null:
		 * 	呼叫搜尋方法 "searchProjectsByCcUp(project)"，查詢方案名稱為'project'的所有資料
		 * 若以上皆非:
		 * 	呼叫搜尋方法 "searchProjectsByThresholdUp(project, cc)"，查詢方案名稱為'project'及排氣量為'cc'的所有資料
		 */
		List<Fee> resList = !StringUtils.hasText(project) ? new ArrayList<>()
				: (cc == null ? feeDao.searchProjectsByCcUp(project) : feeDao.searchProjectsByThresholdUp(project, cc));
		/*
		 * 回傳資料
		 * ->若'resList'內部無資料則回傳"找不到"
		 * ->成功則回傳"成功"並包含資料陣列'resList'
		 */
		return resList.size() == 0 ? new FeeResponse(RtnCode.NOT_FOUND.getMessage())
				: new FeeResponse(resList, RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 費率計算
	 * 輸入參數:
	 * -交通工具物件(Vehicle):vehicle
	 * -是否vip資格(boolean):isVip
	 * -時間跨距(Duration):period
	 */
	@Override
	public FeeResponse calculate(Vehicle vehicle, boolean isVip, Duration period) {
		/*
		 * 防止空輸入
		 * 若'vehicle'或'period'其一為null:
		 * ->則回傳"不得為空"
		 */
		if (vehicle == null || period == null) {
			return new FeeResponse(RtnCode.CANNOT_EMPTY.getMessage());
		}
		/*
		 * 呼叫搜尋方法 "feeDao.searchProjectsByThresholdUp(project, cc)，查詢方案名稱為'project'及排氣量為'cc'的所有資料
		 * 其中'isVip'若為true:
		 * 	則將'vehicle'物件的'category'加上'-vip'並置入方法輸入參數中'project'的位置
		 * 若為false:
		 * 	則置入'vehicle'物件的'category'
		 */
		List<Fee> resList = feeDao.searchProjectsByThresholdUp(
				isVip ? vehicle.getCategory().concat("-vip") : vehicle.getCategory(), vehicle.getCc());
		/*
		 * 若'resList'內部無資料為空:
		 * ->則回傳"找不到"
		 */
		if (CollectionUtils.isEmpty(resList)) {
			return new FeeResponse(RtnCode.NOT_FOUND.getMessage());
		}
		/*
		 * 轉換時間
		 * 以交通工具種類'category'是否為'bike'，轉成適當時間單位並以int型態儲存給'interval'
		 * 'bike'時間單位為minutes ; 其他時間單位為days
		 */
		int interval = vehicle.getCategory().contains("bike") ? (int) period.toMinutes() : (int) period.toDays();
		/*
		 * 未滿時間計算
		 * 以交通工具種類'category'是否為'bike'，執行不滿一時間單位的無條件進位
		 * 方法說明:
		 * 1.將'period'全轉換成毫秒，對以 1 minute/day為單位的毫秒(60,000ms / 86,400,000ms) 求餘數(%)
		 * 2.若存在餘數大於0，則向上取整數(+1)
		 */
		interval += vehicle.getCategory().contains("bike")
				? (period.toNanos() % Duration.ofMinutes(1).toNanos() > 0 ? 1 : 0)
				: (period.toNanos() % Duration.ofDays(1).toNanos() > 0 ? 1 : 0);
		/*
		 * 宣告3個變數
		 * 總金額(double):total
		 * 前次方案費率(double):lastRate
		 * 前次方案時間閾值(int):lastThreshold
		 */
		double total = 0;
		double lastRate = 0;
		int lastThreshold = 0;
		/*
		 * 計算租金
		 * 對'resList'進行迴圈，並以'interval'與'threshold'的大小為判斷條件，進行金額加總
		 * bike計價方式=>方案時間閾值內費率相同，超過時間閾值進入下一方案費率
		 * 其他計價方式 =>未超過方案時間閾值使用前次方案費率，超過當前方案時間閾值，採用當前方案費率進行計價
		 */
		for (int i = 0; i < resList.size(); i++) {
				/*
				 * 若'interval' >= 'threshold':
				 * 則'category'為'bike' --> 當前方案'rate' * 當前方案總時間(當前方案'threshold' - 前次方案'lastThreshold')
				 *    其他 -- > 0
				 */
			if (interval >= resList.get(i).getThreshold()) {
				total += (vehicle.getCategory().contains("bike"))
						? resList.get(i).getRate() * (resList.get(i).getThreshold() - lastThreshold)
						: 0;
				/*
				 * 紀錄當前方案的'rate'至'lastRate'，當前方案的'threshold'至'lastThreshold'
				 * 執行下一次迴圈，進入下一個方案
				 */
				lastRate = resList.get(i).getRate();
				lastThreshold = resList.get(i).getThreshold();
				/* 
				 * 非'interval' >= 'threshold'狀況時:
				 * 則'category'為'bike' --> 當前方案'rate' * 當前方案時間('interval' - 前次方案'lastThreshold')
				 *    其他 -- > 前次方案'lastRate' * 時間'interval'
				 * 並中斷迴圈
				 */
			} else {
				total += (vehicle.getCategory().contains("bike"))
						? resList.get(i).getRate() * (interval - lastThreshold)
						: lastRate * interval;
				break;
			}
		}
		/*
		 * ->成功則回傳"成功"並回傳總金額(四捨五入取整數)
		 */
		return new FeeResponse((int) Math.round(total), RtnCode.SUCCESS.getMessage());
	}

	/*
	 * 顯示所有費率方案
	 * 輸入參數: 無
	 */
	@Override
	public FeeResponse showAllFees() {
		/*
		 * 回傳資料
		 * ->回傳"成功"並執行findAll()回傳所有資料
		 */
		return new FeeResponse(feeDao.findAll(), RtnCode.SUCCESS.getMessage());
	}

}
