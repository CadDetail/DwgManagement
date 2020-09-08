package cn.keyi.bye.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.SysPermissionDao;
import cn.keyi.bye.model.SysPermission;

@Service
public class SysPermissionService {
	
	@Autowired
	SysPermissionDao sysPermissionDao;
	
	// 获取数据库中权限的数量
	public Long getPermissionCount() {
		return sysPermissionDao.count();
	}
	
	// 获取所有角色列表
	public List<SysPermission> findAll() {
		return sysPermissionDao.findAll();
	}
	
	// 根据权限代码查询权限实体
	public SysPermission getPermissionByCode(String permissionCode) {
		List<SysPermission> permissions = sysPermissionDao.findByPermissionCode(permissionCode);
		if(permissions.size() > 0) {
			return permissions.get(0);
		} else {
			return null;
		}
	}
	
	// 以分页的方式查询权限列表
	public Page<SysPermission> getSysPermissionByPage(String permissionTitle, Pageable pageable) {
		return sysPermissionDao.findByPermissionTitleContaining(permissionTitle, pageable);		
	}
	
	// 保存权限
	public String saveSysPermission(SysPermission syspermission) {
		String rslt = "";
		try {
			sysPermissionDao.save(syspermission);
		}catch (Exception e){
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	// 删除权限
	public String deleteSysPermission(Long permissionId) {
		String rslt = "";
		try {
			sysPermissionDao.deleteById(permissionId);
		} catch (Exception e) {		
			rslt = e.getMessage();
		}
		return rslt;
	}	
	
}
