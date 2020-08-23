package cn.keyi.bye.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.Artifact;
import cn.keyi.bye.model.ArtifactDetail;
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
	@RequiresPermissions("detail:del")
	public Object deleteDetail(Long detailId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = artifactDetailService.deleteArtifactDetail(detailId);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "明细删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
	@RequestMapping("/saveArtifactDetail")
	@RequiresPermissions(value={"detail:add","detail:edit"},logical=Logical.OR)
	public Object saveArtifact(HttpServletRequest request) {
		ArtifactDetail detail = new ArtifactDetail();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			detail.setDetailId(id);
		}
		detail.setNumber(Integer.valueOf(request.getParameter("number")));
		detail.setNeedSplit(Boolean.valueOf(request.getParameter("needSplit")));
		detail.setDetailMemo(request.getParameter("detailMemo"));
		Long parentId = Long.valueOf(request.getParameter("parentId"));
		Artifact parent = artifactService.getArtifactById(parentId);
		String artifactCode = request.getParameter("artifactCode");
		Artifact son = artifactService.getArtifactByCode(artifactCode);
		detail.setMaster(parent);
		detail.setSlave(son);
		Map<String, Object> map = new HashMap<String, Object>();
		List<ArtifactDetail> existDetails = artifactDetailService.getDetailByMasterIdAndSlaveId(parentId, son.getArtifactId());
		// 新增模式下, 先判断明细中是不是此零件已在记录中
		if(detail.getDetailId() == null && existDetails.size() > 0) {
			map.put("status", 2);
			map.put("message", "明细中已经包含此零件！");
		} else {
			String rslt = artifactDetailService.saveArtifactDetail(detail);
			if(rslt.isEmpty()) {
				map.put("status", 1);
				map.put("message", "明细保存成功！");
			} else {
				map.put("status", 0);
				map.put("message", rslt);
			}
		}		
		return map;		
	}

	@RequestMapping("/saveArtifactInspect")
	@RequiresPermissions("detail:check")
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
		detail.setQuota(Float.valueOf(request.getParameter("quota")));
		detail.setWorkingSteps(request.getParameter("workingSteps"));
		detail.setClassificationSign(request.getParameter("classificationSign"));
		detail.setProcessSign(request.getParameter("processSign"));
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
