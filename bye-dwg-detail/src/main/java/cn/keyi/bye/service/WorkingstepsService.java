package cn.keyi.bye.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.WorkingstepsDao;
import cn.keyi.bye.model.Workingsteps;

@Service
public class WorkingstepsService {

	@Autowired
	WorkingstepsDao workingstepsDao;
	
	/***
	 * comment: 获取所有工序
	 * author : 兴有林栖
	 * date   : 2020-12-18
	 * @return
	 */
	public List<Workingsteps> getAllWorkingsteps() {
		return workingstepsDao.findAll();
	}
	
	/**
	 * comment: 根据工序ID获取指定工序
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param stepId
	 * @return
	 */
	public Workingsteps getWorkingstepsById(Long stepId) {
		Optional<Workingsteps> findResult = workingstepsDao.findById(stepId);
		if(findResult.isPresent()) {
			return findResult.get();
		} else {
			return null;
		}
	}
	
	/**
	 * comment: 保存工序
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param workingsteps
	 * @return
	 */
	public String saveWorkingsteps(Workingsteps workingsteps) {
		String rslt = "";
		try {
			workingstepsDao.save(workingsteps);
		}catch (Exception e){
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/**
	 * comment: 根据工序ID删除指定工序
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param formulaId
	 * @return
	 */
	public String deleteWorkingsteps(Long formulaId) {
		String rslt = "";
		try {
			workingstepsDao.deleteById(formulaId);
		} catch (Exception e) {		
			rslt = e.getMessage();
		}
		return rslt;
	}
	
}
