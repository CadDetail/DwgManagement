package cn.keyi.bye.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.keyi.bye.model.SysPermission;

public interface SysPermissionDao extends JpaRepository<SysPermission, Long> {
	
	// 根据权限代码查询权限实体
	List<SysPermission> findByPermissionCode(String permissionCode);

}
