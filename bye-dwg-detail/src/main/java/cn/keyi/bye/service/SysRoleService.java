package cn.keyi.bye.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import cn.keyi.bye.dao.SysRoleDao;
import cn.keyi.bye.model.SysRole;

@Service
public class SysRoleService {
	
	public static final String ALL_PERMISSIONS  = "system:all";	// 所有权限
	public static final String ALL_WORKINGSTEPS = "所有工序";		// 所有工序

	@Autowired
	SysRoleDao sysRoleDao;
	
	// 获取所有角色列表
	public List<SysRole> findAll() {
		return sysRoleDao.findAll();
	}
	
	// 根据角色ID查询角色实体
	public SysRole findByRoleId(Long roleId) {
		return sysRoleDao.findByRoleId(roleId);
	}
	
	// 根据角色名称获取角色实体
	public SysRole findByRoleName(String roleName) {
		List<SysRole> roles = sysRoleDao.findByRoleName(roleName);
		if(roles.size() > 0) {
			return roles.get(0);
		} else {
			return null;
		}
	}
	
	// 获取角色数量
	public Long getRoleCount() {
		return sysRoleDao.count();
	}
	
	// 根据角色描述（别名）查找角色列表
	public Page<SysRole> getRolesByPage(String description, Pageable pageable) {
		return sysRoleDao.findByDescriptionContaining(description, pageable);
	}
	
	// 保存角色
	public String saveRole(SysRole role) {
		String rslt = "";
		try {
			sysRoleDao.save(role);
		} catch (Exception e) {
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	// 删除角色
	public String deleteRole(Long roleId) {
		String rslt = "";
		try {
			sysRoleDao.deleteById(roleId);
		} catch (Exception e) {		
			rslt = e.getMessage();
			if(rslt.contains("ConstraintViolationException")) {
				rslt = "违反参照完整性，请先删除从表中的相关记录！";
			}
		}
		return rslt;
	}
	
}
