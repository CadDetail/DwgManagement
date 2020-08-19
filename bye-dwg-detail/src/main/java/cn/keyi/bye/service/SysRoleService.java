package cn.keyi.bye.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.keyi.bye.dao.SysRoleDao;
import cn.keyi.bye.model.SysRole;

@Service
public class SysRoleService {

	@Autowired
	SysRoleDao sysRoleDao;
	
	// 获取所有角色列表
	public List<SysRole> findAll() {
		return sysRoleDao.findAll();
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
}
