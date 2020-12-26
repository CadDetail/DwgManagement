package cn.keyi.bye.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;
import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.model.Needsplitprefix;
import cn.keyi.bye.model.SysRole;
import cn.keyi.bye.service.AccessService;
import cn.keyi.bye.service.ArtifactDetailService;
import cn.keyi.bye.service.ArtifactService;
import cn.keyi.bye.service.NeedsplitprefixService;
import cn.keyi.bye.service.SysRoleService;

@RestController
@RequestMapping("/artifact")
public class ArtifactController {

	@Autowired
	ArtifactService artifactService;
	@Autowired
	ArtifactDetailService artifactDetailService;
	@Autowired
	AccessService accessService;
	@Autowired
	NeedsplitprefixService needsplitprefixService;
	@Autowired
	SysRoleService sysRoleService;
	
	/**
	 * comment: 以分页的方式查询产品（工件）列表
	 * author : 兴有林栖
	 * date   : 2020-8-1
	 * @param request
	 * @return
	 */
	@RequestMapping("/findArtifacts")
	@RequiresPermissions(value={"system:all","artifact:view"},logical=Logical.OR)
	public Object findArtifacts(HttpServletRequest request) {
		int draw = Integer.parseInt(request.getParameter("draw"));			// DataTable 要求要返回的参数
		int pageNumber = Integer.parseInt(request.getParameter("start"));	// 记录起始编号
		int pageSize = Integer.parseInt(request.getParameter("length"));	// 页大小
		pageNumber = pageSize <= 0 ? 1 : pageNumber / pageSize;				// 计算页码
		String artifactCode = request.getParameter("artifactName");			// 工件图号
		String artifactName = request.getParameter("artifactName");			// 工件名称
		short productFlag = (short) Integer.parseInt(request.getParameter("productFlag"));	// 工件类别
		//Integer canSplit = Integer.parseInt(request.getParameter("canBeSplit"));			// 是否可分解
		//Boolean canBeSplit = canSplit == 0 ? true : false;
		String orderColumn = request.getParameter("order[0][column]");		// 排序字段编号
		String orderDir = request.getParameter("order[0][dir]");			// 排序方式
		String orderField = request.getParameter("columns["+orderColumn+"][data]");	//排序字段名称，这里要注意与数据库字段一致
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderDir), orderField));
		Page<Artifact> page = artifactService.getArtifactsByPage(artifactCode, artifactName, productFlag, pageable);
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
	@RequiresPermissions(value={"system:all","detail:view"},logical=Logical.OR)
	public List<Map<String, Object>> findRecursiveDetail(Long masterId) {
		List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
		Artifact master = artifactService.getArtifactById(masterId);
		int times = 1;	// 初始倍数置为1, 即最开始要遍历下级零件的产品看成是1件
		if(master != null) {			
			artifactDetailService.findRecursiveDetailByMaster(master, times, listDetail);
		}
		return listDetail;
	}
	
	/***
	 * comment: 根据给出的工件Id, 以递归的方式获取其下层所有零件，包含重量计算
	 * author : 兴有林栖
	 * date   : 2020-12-19 
	 * @param masterId
	 * @return
	 */
	@RequestMapping("/findSubDetails")
	@RequiresPermissions(value={"system:all","detail:view"},logical=Logical.OR)
	public List<Map<String, Object>> findSubDetails(Long masterId) {
		List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
		Artifact master = artifactService.getArtifactById(masterId);
		int times = 1;	// 初始倍数置为1, 即最开始要遍历下级零件的产品看成是1件
		if(master != null) {			
			listDetail = artifactDetailService.getDetailByMaster(master, times);
		}
		return listDetail;
	}
	
	@RequestMapping("/findSubDetailsWithWorkingsteps")
	@RequiresPermissions(value={"system:all","detail:view"},logical=Logical.OR)
	public List<Map<String, Object>> findSubDetailsWithWorkingsteps(Long masterId) {
		List<Map<String, Object>> listDetail = new ArrayList<Map<String, Object>>();
		Artifact master = artifactService.getArtifactById(masterId);
		int times = 1;	// 初始倍数置为1, 即最开始要遍历下级零件的产品看成是1件
		if(master != null) {			
			listDetail = artifactDetailService.getDetailByMaster(master, times);
		}
		// 取得当前登录用户的所属角色所包含的查询明细时可查询的工序列表
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		HashSet<String> roleList = cookieUser.getRoleList();		
		String steps = "";
		for(String roleName : roleList) {
			SysRole role = sysRoleService.findByRoleName(roleName);
			String tmp = role.getWorkingsteps();
			steps += tmp == null ? "" : tmp;			
		}
		HashSet<String> stepSet = new HashSet<String>(Arrays.asList(steps.split(",")));
		// 重新整理listDetail，过滤没有查询权限的明细
		HashSet<Long> removeTab = new HashSet<Long>();
		List<Map<String, Object>> newListDetail = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> tab : listDetail) {
			Map<String, Object> newTab = new HashMap<String, Object>();
			newTab.put("artifact", tab.get("artifact"));
			newTab.put("times", tab.get("times"));
			newTab.put("weight", tab.get("weight"));
			@SuppressWarnings("unchecked")
			List<ArtifactDetail> details = (List<ArtifactDetail>) tab.get("detail");
			List<ArtifactDetail> newDetails = new ArrayList<ArtifactDetail>();
			for(ArtifactDetail item : details) {
				// 没有查询权限，即用户可查询的工序列表中未出现明细的工序
				if(!stepSet.contains(item.getWorkingSteps()) && !stepSet.contains(SysRoleService.ALL_WORKINGSTEPS)) {
					removeTab.add(item.getSlave().getArtifactId());	// 记录需要从Tab移除的零件ID
				} else {
					newDetails.add(item);
				}
			}
			newTab.put("detail", newDetails);
			newListDetail.add(newTab);
		}
		// 移除没有查询权限的需要放在Tab卡中的零件
		List<Map<String, Object>> rlstListDetail = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> tab : newListDetail) {
			Artifact artifact = (Artifact) tab.get("artifact");
			if(!removeTab.contains(artifact.getArtifactId())) {
				rlstListDetail.add(tab);
			}
		}
		return rlstListDetail;
	}
	
	@RequestMapping("/saveArtifact")
	@RequiresPermissions(value={"system:all","artifact:add","artifact:edit"},logical=Logical.OR)
	public Object saveArtifact(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		Artifact artifact = new Artifact();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			Artifact tmp = artifactService.getArtifactById(id);
			if(tmp != null) {
				artifact = tmp;
			} else {
				artifact.setArtifactId(id);
			}
			artifact.setUpdateBy(cookieUser.getUserName());
			artifact.setUpdateTime(LocalDateTime.now());
		} else {
			artifact.setCreateBy(cookieUser.getUserName());
			artifact.setCreateTime(LocalDateTime.now());
		}
		artifact.setArtifactName(request.getParameter("artifactName"));
		artifact.setArtifactCode(request.getParameter("artifactCode"));
		artifact.setMaterialCode(request.getParameter("materialCode"));
		artifact.setMaterialName(request.getParameter("materialName"));
		artifact.setWeight(Float.valueOf(request.getParameter("weight")));
		String canBeSplit = request.getParameter("canBeSplit");
		if(canBeSplit != null) {
			artifact.setCanBeSplit(canBeSplit.equals("on") ? true : false);	
		} else {
			artifact.setCanBeSplit(true);
		}
		String productFlag = request.getParameter("productFlag");
		if(productFlag != null) {
			artifact.setProductFlag(Short.valueOf(productFlag));
		} else {
			artifact.setProductFlag((short) 0);
		}		
		artifact.setProductModel(request.getParameter("productModel"));
		String artifactMemo = request.getParameter("artifactMemo");
		if(artifactMemo != null) {
			artifact.setArtifactMemo(artifactMemo);
		}		
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
	@RequiresPermissions(value={"system:all","artifact:del"},logical=Logical.OR)
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
	@RequiresPermissions(value={"system:all","detail:import"},logical=Logical.OR)
	public Object importArtifact(HttpServletRequest request) {		
		int mode = Integer.valueOf(request.getParameter("conflictMode"));
		String mdbPhysicalFile = request.getParameter("fileAtSrvr");		
		Map<String, Object> map = new HashMap<String, Object>();
		int rslt = 0;
		try {
			// 获取组件的图号前缀标签, 并区分是顶层产品还是普通组件
			HashSet<String> productPrefix = new HashSet<String>();
			HashSet<String> partPrefix = new HashSet<String>();
			List<Needsplitprefix> prefixList = needsplitprefixService.getNeedsplitprefixs();
			for(Needsplitprefix prefix : prefixList) {
				if(prefix.getPrefixProduct()) {
					productPrefix.add(prefix.getPrefixLabel());
				} else {
					partPrefix.add(prefix.getPrefixLabel());
				}
			}
			// 从Access文件中获取 MXBTL表中存放的零件列表
			List<Artifact> artifacts = accessService.getArtifacts(mdbPhysicalFile, productPrefix, partPrefix);
			HashSet<String> needSplitPrefix = new HashSet<String>();
			needSplitPrefix.clear();
			needSplitPrefix.addAll(productPrefix);
			needSplitPrefix.addAll(partPrefix);
			// 从Access文件中获取 MXSHUJU 表中存放的明细列表
			List<Map<String, Object>> details = accessService.getArtifactDetail(mdbPhysicalFile, needSplitPrefix);
			// ① 从Access文件的 MXSHUJU表中获取明细的所属图号和图号，用于③
			HashSet<String> masterCodes = new HashSet<String>();
			HashSet<String> slaveCodes = new HashSet<String>();
			for(Map<String, Object> detail : details) {
				masterCodes.add(detail.get("parentCode").toString());
				slaveCodes.add(detail.get("sonCode").toString());
			}
			// ② 在图号列表中加入MXBTL表中的图号，用于③
			for(Artifact artifact : artifacts) {
				slaveCodes.add(artifact.getArtifactCode());
			}
			// ③ 判断明细中所属图号对应的组件是否在Access明细表中或在数据库中
			List<String> unfounds = accessService.getUnfoundArtifactCodes(new ArrayList<String>(masterCodes), 
					new ArrayList<String>(slaveCodes), productPrefix);
			if(unfounds.size() == 0) {
				// 开始批量导入明细
				rslt = accessService.batchImportArtifactDetail(artifacts, details, mode, productPrefix);			
				if(rslt == 1) {
					map.put("message", "明细数据导入成功！");
				} else {
					map.put("message", "明细数据导入失败！");
				}
			} else { // 存在不在表中或库中的所属图号
				map.put("message", "请先导入不在库中的零件，它们是：" + String.join("、", unfounds));
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
