package cn.keyi.bye.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.SysPermissionDao;

@Service
public class SysPermissionService {
	
	@Autowired
	SysPermissionDao sysPermissionDao;
	
	// 获取数据库中权限的数量
	public Long getPermissionCount() {
		return sysPermissionDao.count();
	}
	
}
