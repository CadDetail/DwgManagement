package cn.keyi.bye.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.QuotaformulaDao;
import cn.keyi.bye.model.Quotaformula;

@Service
public class QuotaformulaService {

	@Autowired
	QuotaformulaDao quotaformulaDao;
	
	/**
	 * comment: 获取所有材料材质和计算公式
	 * author : 兴有林栖
	 * date   : 2020-12-18
	 * @return
	 */
	public List<Quotaformula> getAllQuotaformula() {
		return quotaformulaDao.findAll();
	}
	
	/**
	 * comment: 根据材料名称获取对应的材料特性
	 * author : 兴有林栖
	 * date   : 2020-12-21 
	 * @param materialName
	 * @param pageable
	 * @return
	 */
	public Page<Quotaformula> getQuotaformulaByPage(String materialName, Pageable pageable) {
		return quotaformulaDao.findByMaterialNameContaining(materialName, pageable);
	}
	
	/**
	 * comment: 根据ID获取指定材料
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param formulaId
	 * @return
	 */
	public Quotaformula getQuotaformulaById(Long formulaId) {
		Optional<Quotaformula> findResult = quotaformulaDao.findById(formulaId);
		if(findResult.isPresent()) {
			return findResult.get();
		} else {
			return null;
		}
	}
	
	/**
	 * comment: 保存材料信息
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param quotaformula
	 * @return
	 */
	public String saveMaterialFeature(Quotaformula quotaformula) {
		String rslt = "";
		try {
			quotaformulaDao.save(quotaformula);
		}catch (Exception e){
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/**
	 * comment: 根据ID删除指定材料
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param formulaId
	 * @return
	 */
	public String deleteMaterialFeature(Long formulaId) {
		String rslt = "";
		try {
			quotaformulaDao.deleteById(formulaId);
		} catch (Exception e) {		
			rslt = e.getMessage();
		}
		return rslt;
	}
}
