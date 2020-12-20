package cn.keyi.bye.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.QuotaformulaDao;
import cn.keyi.bye.model.Quotaformula;

@Service
public class QuotaformulaService {

	@Autowired
	QuotaformulaDao quotaformulaDao;
	
	/***
	 * comment: 获取所有材料材质和计算公式
	 * author : 兴有林栖
	 * date   : 2020-12-18
	 * @return
	 */
	public List<Quotaformula> getAllQuotaformula() {
		return quotaformulaDao.findAll();
	}
}
