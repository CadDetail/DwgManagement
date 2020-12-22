package cn.keyi.bye.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
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

import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.model.Quotaformula;
import cn.keyi.bye.service.QuotaformulaService;

@RestController
@RequestMapping("/materialfeature")
public class QuotaformulaController {

	@Autowired
	QuotaformulaService quotaformulaService;
	
	@RequestMapping("/findQuotaformula")
	public List<Quotaformula> findAllQuotaformula() {
		return quotaformulaService.getAllQuotaformula();
	}
	
	@RequestMapping("/findMaterialFeatures")
	@RequiresPermissions("materialfeature:view")
	public Object findMaterialFeatures(HttpServletRequest request) {
		int draw = Integer.parseInt(request.getParameter("draw"));				// DataTable 要求要返回的参数
		int pageNumber = Integer.parseInt(request.getParameter("start"));		// 记录起始编号
		int pageSize = Integer.parseInt(request.getParameter("length"));		// 页大小
		pageNumber = pageSize <= 0 ? 1 : pageNumber / pageSize;					// 计算页码
		String materialName = request.getParameter("materialName");	// 权限名称
		if (materialName == null) materialName = "";
		String orderColumn = request.getParameter("order[0][column]");			// 排序字段编号
		String orderDir = request.getParameter("order[0][dir]");				// 排序方式
		String orderField = request.getParameter("columns["+orderColumn+"][data]");	//排序字段名称，这里要注意与数据库字段一致
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderDir), orderField));
		Page<Quotaformula> page = quotaformulaService.getQuotaformulaByPage(materialName, pageable);
		// 下面代码为满足DataTable插件要求而进行的组装
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", draw);
		map.put("recordsTotal", page.getTotalElements());
		map.put("recordsFiltered", page.getTotalElements());
		map.put("data", page.getContent());
		return map;
	}
	
	@RequestMapping("/saveMaterialFeature")
	@RequiresPermissions(value={"materialfeature:add","materialfeature:edit"},logical=Logical.OR)
	public Object saveMaterialFeature(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		Quotaformula quotaformula = new Quotaformula();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			Quotaformula tmp = quotaformulaService.getQuotaformulaById(id);
			if(tmp != null) {
				quotaformula = tmp;
			} else {
				quotaformula.setFormulaId(id);
			}
			quotaformula.setUpdateBy(cookieUser.getUserName());
			quotaformula.setUpdateTime(LocalDateTime.now());
		} else {
			quotaformula.setCreateBy(cookieUser.getUserName());
			quotaformula.setCreateTime(LocalDateTime.now());
		}
		quotaformula.setMaterialCode(request.getParameter("materialCode"));
		quotaformula.setMaterialName(request.getParameter("materialName"));
		quotaformula.setFeature(Float.valueOf(request.getParameter("materialFeature")));
		quotaformula.setFormulaFactor(Float.valueOf(request.getParameter("formulaFactor")));
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = quotaformulaService.saveMaterialFeature(quotaformula);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "材料保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;		
	}
	
	@RequestMapping("/deleteMaterialFeature")
	@RequiresPermissions("materialfeature:del")
	public Object deleteMaterialFeature(Long formulaId) {
		Map<String,Object> map = new HashMap<String, Object>();
		String rslt = quotaformulaService.deleteMaterialFeature(formulaId);
		if(rslt.isEmpty()) {
			map.put("status",1);
			map.put("massage","材料删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;
	}
}
