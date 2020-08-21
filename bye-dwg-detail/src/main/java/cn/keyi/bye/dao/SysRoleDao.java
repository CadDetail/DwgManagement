package cn.keyi.bye.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.keyi.bye.model.SysRole;

public interface SysRoleDao extends JpaRepository<SysRole, Long>  {
	
	// 根据角色ID查询角色实体
	SysRole findByRoleId(Long roleId);
	// 根据角色名称获取角色实体
	List<SysRole> findByRoleName(String roleName);	
	// 根据角色描述获取角色列表
	Page<SysRole> findByDescriptionContaining(String description, Pageable pageable);
}
