package cn.keyi.bye.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;
import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.service.ArtifactDetailService;
import cn.keyi.bye.service.ArtifactService;

@RestController
@RequestMapping("/detail")
public class ArtifactDetailController {

	@Autowired
	ArtifactService artifactService;
	
	@Autowired
	ArtifactDetailService artifactDetailService;
	
	@RequestMapping("/findDetailById")
	public Object findDetailById(Long detailId) {
		ArtifactDetail detail = artifactDetailService.getArtifactDetailById(detailId);
		if(detail == null) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 0);
			map.put("message", "未找到指定的明细记录！");
			return map;
		} else {
			return detail.getSlave();
		}
	}
	
	@RequestMapping("/deleteDetail")
	@RequiresPermissions(value={"system:all","detail:del"},logical=Logical.OR)
	public Object deleteDetail(Long detailId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int rslt = 0;
		try {
			rslt = artifactDetailService.deleteArtifactDetail(detailId);
			if(rslt == 1) {
				map.put("message", "明细删除成功！");
			} else {
				map.put("message", "明细删除失败！");
			}
		} catch (Exception e) {
			map.put("message", e.getMessage());
		}
		map.put("status", rslt);
		return map;	
	}
	
	@RequestMapping("/saveArtifactDetail")
	@RequiresPermissions(value={"system:all","detail:add","detail:edit"},logical=Logical.OR)
	public Object saveArtifact(HttpServletRequest request) {
		ArtifactDetail detail = null;		
		Artifact son = null;
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		String id = request.getParameter("id");
		if( id == null) {	// 新增模式
			detail = new ArtifactDetail();			
			son = new Artifact();			
			Long parentId = Long.valueOf(request.getParameter("parentId"));
			Artifact parent = artifactService.getArtifactById(parentId);
			detail.setMaster(parent);
			detail.setCreateBy(cookieUser.getUserName());
			detail.setCreateTime(LocalDateTime.now());
		} else {			// 编辑模式
			Long detailId = Long.parseLong(id);
			detail = artifactDetailService.getArtifactDetailById(detailId);
			son = detail.getSlave();
			detail.setUpdateBy(cookieUser.getUserName());
			detail.setUpdateTime(LocalDateTime.now());
		}
		son.setArtifactCode(request.getParameter("artifactCode"));
		son.setArtifactName(request.getParameter("artifactName"));			
		son.setMaterialCode(request.getParameter("materialCode"));
		son.setMaterialName(request.getParameter("materialName"));
		son.setProductFlag((short) 1);
		son.setWeight(Float.valueOf(request.getParameter("weight")));
		detail.setNumber(Integer.valueOf(request.getParameter("number")));
		detail.setNeedSplit(Boolean.valueOf(request.getParameter("needSplit")));
		detail.setDetailMemo(request.getParameter("detailMemo"));
		Map<String, Object> map = new HashMap<String, Object>();
		int rslt = 0;
		try {
			rslt = artifactDetailService.saveArtifactDetail(son, detail);
			if(rslt == 1) {
				map.put("message", "明细保存成功！");
			} else {
				map.put("message", "明细保存失败！");
			}
		} catch (Exception e) {
			map.put("message", e.getMessage());
		}
		map.put("status", rslt);	
		return map;		
	}

	@RequestMapping("/saveArtifactInspect")
	@RequiresPermissions(value={"system:all","detail:check"},logical=Logical.OR)
	public Object saveArtifactInspect(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(request.getParameter("id") == null) {
			map.put("status", 0);
			map.put("message", "未获取到明细记录的ID！");
			return map;
		}
		Long detailId = Long.parseLong(request.getParameter("id"));
		ArtifactDetail detail = artifactDetailService.getArtifactDetailById(detailId);		
		detail.setDimension(request.getParameter("dimension"));
		detail.setUnit(request.getParameter("unit"));
		detail.setQuota(request.getParameter("quota"));
		detail.setWorkingSteps(request.getParameter("workingSteps"));
		detail.setInspector(request.getParameter("inspector"));
		String rslt = artifactDetailService.saveArtifactDetail(detail);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "会签保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}	
		return map;		
	}
	
}
