package cn.keyi.bye.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.ArtifactDao;
import cn.keyi.bye.model.Artifact;

@Service
public class ArtifactService {
	
	@Autowired
	ArtifactDao artifactDao;
	
	/**
	 * comment: 根据给定id获取对应的工件
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param artifactId: 工件Id
	 * @return
	 */
	public Artifact getArtifactById(Long artifactId) {
		Optional<Artifact> findResult = artifactDao.findById(artifactId);
		if(findResult.isPresent()) {
			return findResult.get();
		} else {
			return null;
		}
	}
	
	/**
	 * comment: 根据图号获取指定零件
	 * author : 兴有林栖
	 * date   : 2020-8-14
	 * @param artifactCode
	 * @return
	 */
	public Artifact getArtifactByCode(String artifactCode) {
		List<Artifact> listArtifact = artifactDao.findByArtifactCode(artifactCode);
		if(listArtifact.size() > 0) {
			return listArtifact.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * /**
	 * comment: 以分页的方式查询产品（工件）列表
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param artifactName: 工件名称，允许空
	 * @param productFlag : 工件类型标识，0-顶层产品，1-普通工件，-1-查询时忽略此参数
	 * @param canBeSplit  : 是否有明细（可分解）
	 * @param pageable
	 * @return
	 */
	public Page<Artifact> getArtifactsByPage(String artifactName, Short productFlag, Boolean canBeSplit, Pageable pageable) {
		if(productFlag == -1) {
			if(!canBeSplit) {
				return artifactDao.findByArtifactNameContaining(artifactName, pageable);
			} else {
				return artifactDao.findByArtifactNameContainingAndCanBeSplit(artifactName, canBeSplit, pageable);
			}			
		} else {
			if(!canBeSplit) {
				return artifactDao.findByArtifactNameContainingAndProductFlag(artifactName, productFlag, pageable);
			} else {
				return artifactDao.findByArtifactNameContainingAndProductFlagAndCanBeSplit(artifactName, productFlag, canBeSplit, pageable);
			}
		}			
	}
	
	/**
	 * comment: 保存零件
	 * author : 兴有林栖
	 * date   : 2020-8-13
	 * @param artifact
	 * @return
	 */
	public String saveArtifact(Artifact artifact) {
		String rslt = "";
		try {
			artifactDao.save(artifact);
		} catch (Exception e) {
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/**
	 * comment: 根据零件ID删除此零件
	 * author : 兴有林栖
	 * date   : 2020-8-14
	 * @param artifactId
	 * @return
	 */
	public String deleteArtifact(Long artifactId) {
		String rslt = "";
		try {
			artifactDao.deleteById(artifactId);
		} catch (Exception e2) {		
			rslt = e2.getMessage();
			if(rslt.contains("ConstraintViolationException")) {
				rslt = "违反参照完整性，请先删除其在明细中的记录！";
			}
		}
		return rslt;
	}
	
}
