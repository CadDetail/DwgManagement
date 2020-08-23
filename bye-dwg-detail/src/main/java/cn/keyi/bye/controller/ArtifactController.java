package cn.keyi.bye.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;
import cn.keyi.bye.service.AccessService;
import cn.keyi.bye.service.ArtifactDetailService;
import cn.keyi.bye.service.ArtifactService;

@RestController
@RequestMapping("/artifact")
public class ArtifactController {

	@Autowired
	ArtifactService artifactService;
	@Autowired
	ArtifactDetailService artifactDetailService;
	@Autowired
	AccessService accessService;
	
	/**
	 * comment: 以分页的方式查询产品（工件）列表
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param artifactName: 工件名称，允许空
	 * @param productFlag : 工件类型标识，0-顶层产品，1-普通工件，-1-查询时忽略此参数
	 * @param pageNumber  : 页码
	 * @param pageSize    : 页大小
	 * @return
	 */
	@RequestMapping("/findArtifacts")
	@RequiresPermissions("artifact:view")
	public Object findArtifacts(HttpServletRequest request) {
		int draw = Integer.parseInt(request.getParameter("draw"));			// DataTable 要求要返回的参数
		int pageNumber = Integer.parseInt(request.getParameter("start"));	// 记录起始编号
		int pageSize = Integer.parseInt(request.getParameter("length"));	// 页大小
		pageNumber = pageSize <= 0 ? 1 : pageNumber / pageSize;				// 计算页码
		String artifactName = request.getParameter("artifactName");			// 工件名称
		short productFlag = (short) Integer.parseInt(request.getParameter("productFlag"));	// 工件类别
		Integer canSplit = Integer.parseInt(request.getParameter("canBeSplit"));			// 是否可分解
		Boolean canBeSplit = canSplit == 0 ? true : false;
		String orderColumn = request.getParameter("order[0][column]");		// 排序字段编号
		String orderDir = request.getParameter("order[0][dir]");			// 排序方式
		String orderField = request.getParameter("columns["+orderColumn+"][data]");	//排序字段名称，这里要注意与数据库字段一致
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderDir), orderField));
		Page<Artifact> page = artifactService.getArtifactsByPage(artifactName, productFlag, canBeSplit, pageable);
		// 下面代码为满足DataTable插件要求而进行的组装
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", draw);
		map.put("recordsTotal", page.getTotalElements());
		map.put("recordsFiltered", page.getTotalElements());
		map.put("data", page.getContent());
		return map;
	}
	
	@RequestMapping("/findArtifactById")
	public Object findArtifactById(Long artifactId) {
		Artifact artifact = artifactService.getArtifactById(artifactId);
		if(artifact == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 0);
			map.put("message", "未找到指定的明细记录！");
			return map;
		} else {
			return artifact;
		}
	}
	
	@RequestMapping("/findArtifactByCode")
	public Object findArtifactByCode(String artifactCode) {
		Artifact artifact = artifactService.getArtifactByCode(artifactCode);
		if(artifact == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 0);
			map.put("message", "未找到指定零件！");
			return map;
		} else {
			return artifact;
		}
	}
	
	@RequestMapping("/findDetailByMasterId")
	public List<ArtifactDetail> findDetailByMasterId(Long masterId) {
		return artifactDetailService.getDetailByMasterId(masterId);
	}
	
	/**
	 * comment: 根据给出的工件Id, 以递归的方式获取其下层所有零件
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param masterId : 指定的工件Id
	 * @return
	 */
	@RequestMapping("/findRecursiveDetail")
	@RequiresPermissions("detail:view")
	public List<Map<String, Object>> findRecursiveDetail(Long masterId) {
		List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
		Artifact master = artifactService.getArtifactById(masterId);
		int times = 1;	// 初始倍数置为1, 即最开始要遍历下级零件的产品看成是1件
		if(master != null) {			
			artifactDetailService.findRecursiveDetailByMaster(master, times, listDetail);
		}
		return listDetail;
	}
	
	@RequestMapping("/saveArtifact")
	@RequiresPermissions(value={"artifact:add","artifact:edit"},logical=Logical.OR)
	public Object saveArtifact(HttpServletRequest request) {
		Artifact artifact = new Artifact();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			artifact.setArtifactId(id);
		}
		artifact.setArtifactName(request.getParameter("artifactName"));
		artifact.setArtifactCode(request.getParameter("artifactCode"));
		artifact.setMaterialName(request.getParameter("materialName"));
		artifact.setWeight(Float.valueOf(request.getParameter("weight")));
		String canBeSplit = request.getParameter("canBeSplit");
		if(canBeSplit != null) {
			artifact.setCanBeSplit(canBeSplit.equals("on") ? true : false);	
		} else {
			artifact.setCanBeSplit(false);
		}
		artifact.setProductFlag(Short.valueOf(request.getParameter("productFlag")));
		artifact.setProductModel(request.getParameter("productModel"));
		artifact.setArtifactMemo(request.getParameter("artifactMemo"));
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = artifactService.saveArtifact(artifact);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "零件保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;		
	}
	
	@RequestMapping("/deleteArtifact")
	@RequiresPermissions("artifact:del")
	public Object deleteArtifact(Long artifactId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = artifactService.deleteArtifact(artifactId);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "零件删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
	@RequestMapping("/importArtifact")
	@RequiresPermissions("detail:import")
	public Object importArtifact(HttpServletRequest request) {		
		int mode = Integer.valueOf(request.getParameter("conflictMode"));
		String mdbPhysicalFile = request.getParameter("fileAtSrvr");		
		Map<String, Object> map = new HashMap<String, Object>();
		int rslt = 0;
		try {
			List<Artifact> artifacts = accessService.getArtifacts(mdbPhysicalFile);
			List<Map<String, Object>> details = accessService.getArtifactDetail(mdbPhysicalFile);
			rslt = accessService.batchImportArtifactDetail(artifacts, details, mode);			
			if(rslt == 1) {
				map.put("message", "明细数据导入成功！");
			} else {
				map.put("message", "明细数据导入失败！");
			}
		} catch (Exception e) {
			map.put("message", e.getMessage());
		}
		map.put("status", rslt);
		//最后, 不管导入成功不成功, 都把上传的mdb文件删了
		accessService.deleteUploadMdbFile(mdbPhysicalFile);
		return map;
	}
	
	@RequestMapping("/deleteUploadMdbFile")
	public Object deleteUploadMdbFile(HttpServletRequest request) {
		String mdbPhysicalFile = request.getParameter("fileAtSrvr");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			accessService.deleteUploadMdbFile(mdbPhysicalFile);
			map.put("status", 1);
			map.put("message", "文件删除成功！");
		} catch(Exception e) {
			map.put("status", 0);
			map.put("message", e.getMessage());
		}
		return map;		
	}
	
	
}
