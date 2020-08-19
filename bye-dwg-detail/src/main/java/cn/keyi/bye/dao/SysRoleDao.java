package cn.keyi.bye.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.keyi.bye.model.SysRole;

public interface SysRoleDao extends JpaRepository<SysRole, Long>  {
	
	// 根据角色名称获取角色实体
	List<SysRole> findByRoleName(String roleName);
}
