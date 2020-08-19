package cn.keyi.bye.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.SysRole;
import cn.keyi.bye.service.SysRoleService;

@RestController
@RequestMapping("/roleInfo")
public class SysRoleController {
	
	@Autowired
	SysRoleService sysRoleService;
	
	// 获取所有角色列表
	@RequestMapping("/getAllRoles")
	public Object getAllRoles() {
		return sysRoleService.findAll();
	}
	
	@RequestMapping("/getRolesForSelect2")
	public Object getRolesForSelect2() {		
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<SysRole> roleList = sysRoleService.findAll();
		for(SysRole role: roleList) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", role.getRoleName());
			item.put("text", role.getDescription());
			items.add(item);
		}
		return items;
	}
}
