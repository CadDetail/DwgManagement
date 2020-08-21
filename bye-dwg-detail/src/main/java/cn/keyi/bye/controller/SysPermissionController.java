package cn.keyi.bye.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
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
	@RequiresPermissions(value={"role:view","permission:view"},logical=Logical.OR)
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
}
