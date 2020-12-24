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

import cn.keyi.bye.model.SysPermission;
import cn.keyi.bye.service.SysPermissionService;

@RestController
@RequestMapping("/permissionInfo")
public class SysPermissionController {

	@Autowired
	SysPermissionService sysPermissionService;
	
	@RequestMapping("/getPermissionsForSelect2")
	@RequiresPermissions(value={"system:all","role:view","permission:view"},logical=Logical.OR)
	public Object getPermissionsForSelect2() {		
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<SysPermission> permissionList = sysPermissionService.findAll();
		for(SysPermission permission: permissionList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", permission.getPermissionCode());
			item.put("text", permission.getPermissionTitle());
			items.add(item);
		}
		return items;
	}
	
	// 查找权限
	@RequestMapping("/findSysPermissions")
	@RequiresPermissions(value={"system:all","permission:view"},logical=Logical.OR)
	public Object findSysPermissions(HttpServletRequest request) {
		int draw = Integer.parseInt(request.getParameter("draw"));				// DataTable 要求要返回的参数
		int pageNumber = Integer.parseInt(request.getParameter("start"));		// 记录起始编号
		int pageSize = Integer.parseInt(request.getParameter("length"));		// 页大小
		pageNumber = pageSize <= 0 ? 1 : pageNumber / pageSize;					// 计算页码
		String sysPermissionTitle = request.getParameter("permissionTitle");	// 权限名称
		if (sysPermissionTitle == null) sysPermissionTitle = "";
		String orderColumn = request.getParameter("order[0][column]");			// 排序字段编号
		String orderDir = request.getParameter("order[0][dir]");				// 排序方式
		String orderField = request.getParameter("columns["+orderColumn+"][data]");	//排序字段名称，这里要注意与数据库字段一致
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(orderDir), orderField));
		Page<SysPermission> page = sysPermissionService.getSysPermissionByPage(sysPermissionTitle, pageable);
		// 下面代码为满足DataTable插件要求而进行的组装
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("draw", draw);
		map.put("recordsTotal", page.getTotalElements());
		map.put("recordsFiltered", page.getTotalElements());
		map.put("data", page.getContent());
		return map;
	}

	// 保存权限
	@RequestMapping("/saveSysPermission")
	@RequiresPermissions(value={"system:all","permission:add","permission:edit"},logical=Logical.OR)
	public Object saveSysPermission(HttpServletRequest request) {
		SysPermission sysPermission = new SysPermission();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			sysPermission.setPermissionId(id);
		}
		sysPermission.setPermissionTitle(request.getParameter("permissionTitle"));
		sysPermission.setPermissionCode(request.getParameter("permissionCode"));
		sysPermission.setPermissionAvailable(Boolean.valueOf(request.getParameter("permissionAvailable")));
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = sysPermissionService.saveSysPermission(sysPermission);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "权限保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;		
	}
	
	// 删除权限
	@RequestMapping("/deleteSysPermission")
	@RequiresPermissions(value={"system:all","permission:del"},logical=Logical.OR)
	public Object deleteSysPermission(Long permissionId) {
		Map<String,Object> map = new HashMap<String, Object>();
		String rslt = sysPermissionService.deleteSysPermission(permissionId);
		if(rslt.isEmpty()) {
			map.put("status",1);
			map.put("massage","权限删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;
	}	

}
