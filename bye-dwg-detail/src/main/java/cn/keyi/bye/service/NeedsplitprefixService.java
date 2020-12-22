package cn.keyi.bye.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.NeedsplitprefixDao;
import cn.keyi.bye.model.Needsplitprefix;

@Service
public class NeedsplitprefixService {

	@Autowired
	NeedsplitprefixDao needsplitprefixDao;
	
	/**
	 * comment: 获取所有具有下级零件的图号标签
	 * author : 兴有林栖
	 * date   : 2020-12-18 
	 * @return
	 */
	public List<Needsplitprefix> getNeedsplitprefixs() {
		return needsplitprefixDao.findAll();
	}
	
	/**
	 * comment: 根据ID获取指定具有下级零件的图号标签
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param stepId
	 * @return
	 */
	public Needsplitprefix getNeedsplitprefixById(Long prefixId) {
		Optional<Needsplitprefix> findResult = needsplitprefixDao.findById(prefixId);
		if(findResult.isPresent()) {
			return findResult.get();
		} else {
			return null;
		}
	}
	
	/**
	 * comment: 保存具有下级零件的图号标签
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param needsplitprefix
	 * @return
	 */
	public String saveNeedsplitprefix(Needsplitprefix needsplitprefix) {
		String rslt = "";
		try {
			needsplitprefixDao.save(needsplitprefix);
		}catch (Exception e){
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/**
	 * comment: 根据ID删除指定具有下级零件的图号标签
	 * author : 兴有林栖
	 * date   : 2020-12-22 
	 * @param prefixId
	 * @return
	 */
	public String deleteNeedsplitprefix(Long prefixId) {
		String rslt = "";
		try {
			needsplitprefixDao.deleteById(prefixId);
		} catch (Exception e) {		
			rslt = e.getMessage();
		}
		return rslt;
	}
}
