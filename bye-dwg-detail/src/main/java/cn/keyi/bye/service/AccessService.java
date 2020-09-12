package cn.keyi.bye.service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.keyi.bye.dao.ArtifactDao;
import cn.keyi.bye.dao.ArtifactDetailDao;
import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;

@Service
public class AccessService {
	
	@Autowired
	ArtifactDao artifactDao;
	@Autowired
	ArtifactDetailDao artifactDetailDao;
	
	// 从Access文件中获取零件列表
	public List<Artifact> getArtifacts(String mdbFileName) {
		List<Artifact> artifacts = new ArrayList<Artifact>();			
		try {
			// 这个驱动的地址不要改
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:ucanaccess://" + mdbFileName);
				Statement stmt = conn.createStatement();
				HashSet<String> parentCodes = new HashSet<String>();
				HashSet<String> sonCodes = new HashSet<String>();
				ResultSet rsDetail = stmt.executeQuery("Select ssdhao, thao From MXSHUJU");
				while(rsDetail.next()) {
					parentCodes.add(rsDetail.getNString("ssdhao"));
					String thao = rsDetail.getNString("thao");
					if(thao == null || thao.isEmpty()) { continue; }
					sonCodes.add(thao);
				}
				ResultSet rsArtifact = stmt.executeQuery("Select * From MXBTL");
				while(rsArtifact.next()) {
					Artifact artifact = new Artifact();
					String artifactCode = rsArtifact.getNString("THAO");
					artifact.setArtifactCode(artifactCode);
					artifact.setArtifactName(rsArtifact.getNString("TYMING"));
					artifact.setProductModel(rsArtifact.getNString("CHPXHAO"));
					artifact.setMaterialCode(rsArtifact.getNString("CLDMA"));
					artifact.setMaterialName(rsArtifact.getNString("CLMING"));
					String weight = rsArtifact.getNString("ZHLIANG");
					try { 
						artifact.setWeight(Float.valueOf(weight));
					} catch(Exception e) {
						artifact.setWeight(null);
					}
					// 在明细表中的所属代号（ssdhao）列能找到此零件的图号，说明它拥有下级零件，否则没有
					if(parentCodes.contains(artifactCode)) {
						artifact.setCanBeSplit(true);
					} else {
						artifact.setCanBeSplit(false);
					}
					// 在明细表中的所属代号（ssdhao）列能找到此零件的图号，但是在图号（thao）列找不到此零件的图号，
					// 说明此零件是顶层拆分的零件，认定其为“产品”级的工件
					if(parentCodes.contains(artifactCode) && !sonCodes.contains(artifactCode)) {
						artifact.setProductFlag((short) 0);
					} else {
						artifact.setProductFlag((short) 1);
					}
					artifacts.add(artifact);
				}
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return artifacts;
	}
	
	// 从Access文件中获取明细记录，以Map方式组织成列表
	public List<Map<String, Object>> getArtifactDetail(String mdbFileName) {
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();			
		try {
			// 这个驱动的地址不要改
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection conn;
			try {
				conn = DriverManager.getConnection("jdbc:ucanaccess://" + mdbFileName);
				Statement stmt = conn.createStatement();
				HashSet<String> parentCodes = new HashSet<String>();
				ResultSet rsDetail = stmt.executeQuery("Select ssdhao From MXSHUJU");
				while(rsDetail.next()) {
					parentCodes.add(rsDetail.getNString("ssdhao"));
				}
				rsDetail = stmt.executeQuery("Select * From MXSHUJU");
				while(rsDetail.next()) {
					Map<String, Object> detail = new HashMap<String, Object>();
					String parentCode = rsDetail.getNString("ssdhao");
					String sonCode = rsDetail.getNString("thao");
					String artifactName = rsDetail.getNString("mcheng");
					String materialCode = rsDetail.getNString("cldma");
					String number = rsDetail.getNString("shliang");
					String weight = rsDetail.getNString("zhliang");
					String detailMemo = rsDetail.getNString("bzhbzhi");
					sonCode = (sonCode == null) ? "" : sonCode;
					artifactName = (artifactName == null) ? "" : artifactName;
					materialCode = (materialCode == null) ? "" : materialCode;
					// 数量如果导入时发现为空, 则将其设置为默认值1
					number = (number == null) ? "1" : number;
					// 跳过Access中可能存在的空记录行
					if(String.join("", sonCode, artifactName, materialCode).isEmpty()) {
						continue;
					}					
					detail.put("parentCode", parentCode);
					detail.put("sonCode", sonCode);					
					try {
						detail.put("number", Integer.valueOf(number));
					} catch(Exception e) {
						detail.put("number", 0);	// 读出的内容如果不是数字字符串, 则用0代替
					}					
					// 在明细表中的所属代号（ssdhao）列能找到此零件的图号，说明它拥有下级零件，否则没有
					if(parentCodes.contains(sonCode)) {
						detail.put("needSplit", true);						
					} else {
						detail.put("needSplit", false);
					}
					// 如果图号（thao）为空或者库中不存在，需要将明细中的有关记录插入到artifact表中，此时需要零件名称和材料代码等信息
					detail.put("artifactName", artifactName);
					detail.put("materialCode", materialCode);
					try {
						detail.put("weight", Float.valueOf(weight));
					} catch(Exception e) {
						detail.put("weight", 0);	// 读出的内容如果不是数字字符串, 则用0代替
					}
					// 备注
					detail.put("detailMemo", detailMemo);
					detailList.add(detail);
				}
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return detailList;
	}	
	
	// 遍历从Access文件中获取明细记录，检查图号（thao）对应的零件在库中是否已经存在，如果不存在，则先将其插入到artifact表中
	public int traversingArtifactDetail(List<Map<String, Object>> detailList) {
		int rslt = 0;
		try {
			for(Map<String, Object> map : detailList) {
				String artifactCode = map.get("sonCode").toString();
				String artifactName = map.get("artifactName").toString();
				String materialCode = map.get("materialCode").toString();
				List<Artifact> existArtifacts = null;
				if(artifactCode.isEmpty()) {
					existArtifacts = artifactDao.findByArtifactNameAndMaterialCode(artifactName, materialCode);
				} else {
					existArtifacts = artifactDao.findByArtifactCode(artifactCode);				
				}
				if(existArtifacts.size() == 0) {
					Artifact artifact = new Artifact();
					artifact.setArtifactCode(artifactCode);
					artifact.setArtifactName(artifactName);
					artifact.setWeight(Float.valueOf(map.get("weight").toString()));
					artifact.setMaterialCode(materialCode);
					artifact.setProductFlag((short) 1); 
					// 如果 needSplit 值为 true，则表明其可拆分，其它情况默认为不可拆分
					artifact.setCanBeSplit((Boolean) map.get("needSplit"));	
					artifactDao.save(artifact);
				}
			}
			rslt =  1;			
		} catch(Exception e) {
			throw e;
		}
		return rslt;
	}
	
	

	// 批量插入零件, 忽略冲突零件
	@Transactional(rollbackFor = Exception.class)
	public int insertArtifactsIgnoringConflicts(List<Artifact> artifacts) {
		int rslt = 0;
		try {
			for(Artifact artifact: artifacts) {
				List<Artifact> existArtifacts = artifactDao.findByArtifactCode(artifact.getArtifactCode());
				if(existArtifacts.size() == 0) {
					artifactDao.save(artifact);
				}
			}
			rslt = 1;
		} catch(Exception e) {			
			throw e;
		}
		return rslt;
	}
	
	// 批量插入零件, 覆盖库中零件
	@Transactional(rollbackFor = Exception.class)
	public int insertArtifactsCoveringConflicts(List<Artifact> artifacts) {
		int rslt = 0;
		try {
			for(Artifact artifact: artifacts) {
				List<Artifact> existArtifacts = artifactDao.findByArtifactCode(artifact.getArtifactCode());
				if(existArtifacts.size() > 0) {
					Artifact oldArtifact = existArtifacts.get(0);
					oldArtifact.setArtifactName(artifact.getArtifactName());
					oldArtifact.setWeight(artifact.getWeight());
					oldArtifact.setMaterialCode(artifact.getMaterialCode());
					oldArtifact.setMaterialName(artifact.getMaterialName());					
					oldArtifact.setProductFlag(artifact.getProductFlag());
					oldArtifact.setProductModel(artifact.getProductModel());
					oldArtifact.setCanBeSplit(artifact.getCanBeSplit());
					oldArtifact.setArtifactMemo(artifact.getArtifactMemo());
					artifactDao.save(oldArtifact);
				} else {
					artifactDao.save(artifact);
				}
			}
			rslt = 1;
		} catch(Exception e) {			
			throw e;
		}
		return rslt;
	}
	
	// 批量插入明细记录, 忽略冲突零件(冲突: 表中存在相同的parentArtifactCode和aritfactCode)
	@Transactional(rollbackFor = Exception.class)
	public int insertDetailIgnoringConflicts(List<Map<String, Object>> detailList) {
		int rslt = 0;
		try {
			for(Map<String, Object> detail: detailList) {
				String masterCode = (String) detail.get("parentCode");
				String slaveCode = (String) detail.get("sonCode");
				String slaveName = (String) detail.get("artifactName");
				String slaveMaterial = (String) detail.get("materialCode");
				List<ArtifactDetail> existDetailList;
				if(slaveCode.isEmpty()) {	// 明细中图号（thao）为空
					existDetailList = artifactDetailDao.findByMasterArtifactCodeAndSlaveArtifactNameAndSlaveMaterialCode(masterCode, slaveName, slaveMaterial);
				} else {
					existDetailList = artifactDetailDao.findByMasterArtifactCodeAndSlaveArtifactCode(masterCode, slaveCode);
				}				
				if(existDetailList.size() == 0) {
					ArtifactDetail artifactDetail = new ArtifactDetail();
					List<Artifact> masters = artifactDao.findByArtifactCode(masterCode);
					if(masters.size() > 0) {
						artifactDetail.setMaster(masters.get(0));
					}
					List<Artifact> slaves;
					if(slaveCode.isEmpty()) {
						slaves = artifactDao.findByArtifactNameAndMaterialCode(slaveName, slaveMaterial);
					} else {
						slaves = artifactDao.findByArtifactCode(slaveCode);
					}
					if(slaves.size() > 0) {
						artifactDetail.setSlave(slaves.get(0));
					}
					artifactDetail.setNumber((Integer) detail.get("number"));
					artifactDetail.setNeedSplit((Boolean) detail.get("needSplit"));
					artifactDetail.setDetailMemo((String) detail.get("detailMemo"));
					artifactDetailDao.save(artifactDetail);
				}
			}
			rslt = 1;
		} catch(Exception e) {
			throw e;
		}
		return rslt;
	}
	
	// 批量插入明细记录, 覆盖库中零件(冲突: 表中存在相同的parentArtifactCode和aritfactCode)
	@Transactional(rollbackFor = Exception.class)
	public int insertDetailCoveringConflicts(List<Map<String, Object>> detailList) {
		int rslt = 0;
		try {
			for(Map<String, Object> detail: detailList) {
				String masterCode = (String) detail.get("parentCode");
				String slaveCode = (String) detail.get("sonCode");
				String slaveName = (String) detail.get("artifactName");
				String slaveMaterial = (String) detail.get("materialCode");
				List<ArtifactDetail> existDetailList;
				if(slaveCode.isEmpty()) {	// 明细中图号（thao）为空
					existDetailList = artifactDetailDao.findByMasterArtifactCodeAndSlaveArtifactNameAndSlaveMaterialCode(masterCode, slaveName, slaveMaterial);
				} else {
					existDetailList = artifactDetailDao.findByMasterArtifactCodeAndSlaveArtifactCode(masterCode, slaveCode);
				}				
				ArtifactDetail artifactDetail;
				if(existDetailList.size() == 0) {
					artifactDetail = new ArtifactDetail();
					List<Artifact> masters = artifactDao.findByArtifactCode(masterCode);
					if(masters.size() > 0) {
						artifactDetail.setMaster(masters.get(0));
					}
					List<Artifact> slaves;
					if(slaveCode.isEmpty()) {
						slaves = artifactDao.findByArtifactNameAndMaterialCode(slaveName, slaveMaterial);
					} else {
						slaves = artifactDao.findByArtifactCode(slaveCode);
					}
					if(slaves.size() > 0) {
						artifactDetail.setSlave(slaves.get(0));
					}					
				} else {
					artifactDetail = existDetailList.get(0);
				}
				artifactDetail.setNumber((Integer) detail.get("number"));
				artifactDetail.setNeedSplit((Boolean) detail.get("needSplit"));
				artifactDetail.setDetailMemo((String) detail.get("detailMemo"));
				artifactDetailDao.save(artifactDetail);
			}
			rslt = 1;
		} catch(Exception e) {
			throw e;
		}
		return rslt;
	}
	
	// 批量导入零件和明细
	@Transactional(rollbackFor = Exception.class)
	public int batchImportArtifactDetail(List<Artifact> artifacts, List<Map<String, Object>> details, int mode) throws Exception {
		int rslt = 0;		
		// mode: 1-忽略冲突零件; 2-覆盖库中零件
		int artifactOk = (mode == 1) ? insertArtifactsIgnoringConflicts(artifacts) : insertArtifactsCoveringConflicts(artifacts);
		if(artifactOk == 1) {				
			// 先遍历明细记录, 检查是否有些零件先预先插入到 artifact 表中
			int traversed = traversingArtifactDetail(details);
			if(traversed == 1) {				
				int detailOk = (mode == 1) ? insertDetailIgnoringConflicts(details) : insertDetailCoveringConflicts(details);
				if(detailOk == 1) {
					rslt = 1;
				} else {
					throw new Exception("明细导入失败！");
				}
			}
			else {
				throw new Exception("处理不在库中的明细异常！");
			}
		} else {
			throw new Exception("零件导入失败！");
		}		
		return rslt;
	}
	
	// 删除指定文件, 用于在导入明细数据后把上传的文件删除
	public void deleteUploadMdbFile(String mdbFile) {
		File uploadFile = new File(mdbFile);
		if(uploadFile.isFile()) {
			uploadFile.delete();
		}
	}
	
}
