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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.SysPermission;
import cn.keyi.bye.model.SysRole;
import cn.keyi.bye.service.SysPermissionService;
import cn.keyi.bye.service.SysRoleService;

@RestController
@RequestMapping("/roleInfo")
public class SysRoleController {
	
	@Autowired
	SysRoleService sysRoleService;
	@Autowired
	SysPermissionService sysPermissionService;
	
	// 获取所有角色列表
	@RequestMapping("/getAllRoles")
	@RequiresPermissions(value={"user:view","role:view"},logical=Logical.OR)
	public Object getAllRoles() {
		return sysRoleService.findAll();
	}
	
	@RequestMapping("/getRolesForSelect2")
	@RequiresPermissions(value={"user:view","role:view"},logical=Logical.OR)
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
	
	@RequestMapping("/getRoles")
	@RequiresPermissions("role:view")
	public Object getRoles(HttpServletRequest request) {
		Integer pageNumber = Integer.valueOf(request.getParameter("pageNumber")) - 1;
		Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
		if(pageNumber < 0) { pageNumber = 0; }
		String roleAlias = request.getParameter("roleAlias");
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.DESC, "roleId"));
		Page<SysRole> roles = sysRoleService.getRolesByPage(roleAlias, pageable);
		return roles;
	}
	
	@RequestMapping("/saveRole")
	@RequiresPermissions(value={"role:add","role:edit"},logical=Logical.OR)
	public Object saveRole(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysRole role = null;
		// 修改模式
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			role = sysRoleService.findByRoleId(id);	
			if(role == null) {
				map.put("status", 0);
				map.put("message", "角色不存在，请重试！");
				return map;
			}
		}
		// 新增模式
		if(role == null) { 
			role = new SysRole();
			String name = request.getParameter("roleName");
			SysRole existRole = sysRoleService.findByRoleName(name);
			if(existRole != null) {
				map.put("status", 0);
				map.put("message", "角色已存在，请重试！");
				return map;
			}
			role.setRoleName(name);
			role.setRoleAvailable(true);
		}
		// 新增和修改模式下，都需要设置角色别名、拥有权限		
		role.setDescription(request.getParameter("description"));		
		String permissions = request.getParameter("rolePermission");
		List<SysPermission> permissionList = new ArrayList<SysPermission>();
		for(String permissionCode: permissions.split(",")) {
			SysPermission permission = sysPermissionService.getPermissionByCode(permissionCode);
			if(permission != null) {
				permissionList.add(permission);
			}
		}	
		role.setPermissions(permissionList);
		String rslt = sysRoleService.saveRole(role);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "角色保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
	@RequestMapping("/deleteRole")
	@RequiresPermissions("role:del")
	public Object deleteRole(Long roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = sysRoleService.deleteRole(roleId);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "角色删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
}
