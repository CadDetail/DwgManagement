package cn.keyi.bye.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.keyi.bye.dao.ArtifactDao;
import cn.keyi.bye.dao.ArtifactDetailDao;
import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;

@Service
public class ArtifactDetailService {

	@Autowired
	ArtifactDetailDao artifactDetailDao;
	@Autowired
	ArtifactDao artifactDao;
	
	/**
	 * comment: 根据一个明细ID获取该明细记录
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param detailId
	 * @return
	 */
	public ArtifactDetail getArtifactDetailById(Long detailId) {
		Optional<ArtifactDetail> findResult = artifactDetailDao.findById(detailId);
		if(findResult.isPresent()) {
			return findResult.get();
		} else {
			return null;
		}
	}
	
	// 根据指定的工件ID查询其包含的下层（单层）工件组成明细
	public List<ArtifactDetail> getDetailByMasterId(Long masterId) {
		return artifactDetailDao.findByMasterArtifactId(masterId);
	}
	
	// 根据指定的工件ID以分页的形式查询其包含的下层（单层）工件组成明细
	public Page<ArtifactDetail> getDetailByMasterId(Long masterId, Pageable pageable) {
		return artifactDetailDao.findByMasterArtifactId(masterId, pageable);
	}
	
	/**
	 * comment: 根据parentArtifactId和artifactId查询明细记录
	 * author : 兴有林栖
	 * date   : 2020-8-15
	 * @param masterId
	 * @param salveId
	 * @return
	 */
	public List<ArtifactDetail> getDetailByMasterIdAndSlaveId(Long masterId, Long slaveId) {
		return artifactDetailDao.findByMasterArtifactIdAndSlaveArtifactId(masterId, slaveId);
	}
	
	/**
	 * comment: 指定一个工件，使用递归方式获取从属于它的所有下级工件，并组装成一个列表
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param master: 遍历的顶层零件（出发点）
	 * @param times : 倍数，即层级之间的零件数量倍数
	 * @param recursiveDetail: 存储着各级包含下级零件的列表对象
	 */
	public void findRecursiveDetailByMaster(Artifact master, int times, List<Map<String, Object>> recursiveDetail) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("artifact", master);
		List<ArtifactDetail> detail = this.getDetailByMasterId(master.getArtifactId());
		map.put("detail", detail);
		map.put("times", times);
		map.put("weight", 0);
		recursiveDetail.add(map);
		for(ArtifactDetail detailItem : detail) {
			if(detailItem.getNeedSplit()) {
				int newtimes = times * detailItem.getNumber();
				findRecursiveDetailByMaster(detailItem.getSlave(), newtimes, recursiveDetail);
			}			
		}
		return;
	}
	
	/**
	 * comment: 指定一个工件，使用递归方式获取从属于它的所有下级工件，并计算每一个包含下层零件的零件重量，最后组装成一个列表返回
	 * author : 兴有林栖
	 * date   : 2020-12-19 
	 * @param master: 遍历的顶层零件（出发点）
	 * @param times : 倍数，即层级之间的零件数量倍数
	 * @return
	 */
	public List<Map<String, Object>> getDetailByMaster(Artifact master, int times) {
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		// 先调用递归方法获取某产品的所有明细
		this.findRecursiveDetailByMaster(master, times, details);
		// 针对获取的明细，重新计算各具有下级明细的零件的重量
		for(Map<String, Object> map : details) {
			Float weight = this.countWeight(((Artifact) map.get("artifact")).getArtifactId(), details);
			// 四舍五入保留两位小数
			BigDecimal bgWeight = new BigDecimal(weight).setScale(2, RoundingMode.UP);
			// 更新程序中的零件的重量
			Artifact artifact = (Artifact) map.get("artifact");
			artifact.setWeight(bgWeight.floatValue());
			map.put("artifact", artifact);
			map.put("weight", bgWeight.floatValue());
		}
		return details;
	}
	
	/**
	 * comment: 指定一个工件，使用递归方式计算其重量并返回
	 * author : 兴有林栖
	 * date   : 2020-12-19 
	 * @param artifactId: 工件ID
	 * @param details   : 使用递归方式已经获取到的包括自身和下层零件的列表
	 * @return
	 */
	public Float countWeight(Long artifactId, List<Map<String, Object>> details) {
		Float weight = (float) 0.0;
		for(Map<String, Object> map : details) {
			if(((Artifact) map.get("artifact")).getArtifactId() == artifactId) {
				@SuppressWarnings("unchecked")
				List<ArtifactDetail> subDetails = (List<ArtifactDetail>) map.get("detail");
				for(ArtifactDetail detail : subDetails) {
					if(detail.getNeedSplit()) {	// 递归方式计算还包含下层零件的零件重量
						weight = weight + countWeight(detail.getSlave().getArtifactId(), details) * detail.getNumber();
					} else {	// 累计明细中没有下层零件的零件重量
						Float tmpWeight = detail.getSlave().getWeight();
						if(tmpWeight == null) { tmpWeight = (float) 0.0; }
						weight = weight + tmpWeight * detail.getNumber();
					}
				}
				break;
			}
		}
		return weight;
	}
	
	/**
	 * comment: 删除指定的明细记录
	 * author : 兴有林栖
	 * date   : 2020-8-14
	 * @param detailId
	 * @return
	 */
//	public String deleteArtifactDetail(Long detailId) {
//		String rslt = "";
//		try {
//			artifactDetailDao.deleteById(detailId);
//		} catch (Exception e) {		
//			rslt = e.getMessage();
//		}
//		return rslt;
//	}
	
	/**
	 * comment: 删除指定的明细记录，，操作的同时要级联删除明细零件
	 * author : 兴有林栖
	 * date   : 2020-12-12
	 * @param detailId
	 * @return
	 * @throws Exception 
	 */
	@Transactional(rollbackFor = Exception.class)
	public int deleteArtifactDetail(Long detailId) throws Exception {
		int rslt = 0;
		ArtifactDetail detail = this.getArtifactDetailById(detailId);
		if(detail == null) {
			throw new Exception("未找到明细记录！");
		} else {
			try {
				artifactDao.deleteById(detail.getSlave().getArtifactId());
				try {
					artifactDetailDao.deleteById(detailId);
					rslt = 1;
				} catch (Exception e) {		
					throw e;
				}
			} catch (Exception e) {
				throw e;
			}			
		}		
		return rslt;
	}
	
	/**
	 * comment: 添加或编辑某产品的明细
	 * author : 兴有林栖
	 * date   : 2020-8-14
	 * @param detailId
	 * @return
	 */
	public String saveArtifactDetail(ArtifactDetail artifactDetail) {
		String rslt = "";
		try {
			artifactDetailDao.save(artifactDetail);
		} catch (Exception e) {
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/***
	 * comment: 添加或编辑某产品的明细，操作的同时要同步更新明细零件
	 * author : 兴有林栖
	 * date   : 2020-12-12 
	 * @param slave
	 * @param artifactDetail
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public int saveArtifactDetail(Artifact slave, ArtifactDetail artifactDetail) throws Exception {
		int rslt = 0;
		Artifact newArtifact = null;
		try {
			newArtifact = artifactDao.save(slave);			
		} catch (Exception e) {
			throw e;
		}
		if(newArtifact == null) {
			throw new Exception("保存零件异常！");
		} else {
			artifactDetail.setSlave(newArtifact);
			try {
				artifactDetailDao.save(artifactDetail);
				rslt = 1;
			} catch (Exception e) {
				throw e;
			}			
		}
		return rslt;
	}

}
