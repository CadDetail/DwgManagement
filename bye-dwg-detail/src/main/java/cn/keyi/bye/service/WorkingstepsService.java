package cn.keyi.bye.service;

import java.util.List;

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
	
}
