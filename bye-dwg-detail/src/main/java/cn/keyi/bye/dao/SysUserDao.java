package cn.keyi.bye.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.keyi.bye.model.SysUser;

public interface SysUserDao extends JpaRepository<SysUser, Long>  {
	
	// 根据用户账户获取该用户信息
	public SysUser findByUserName(String userName);
	// 根据用户姓名查询用户列表
	public Page<SysUser> findByUserAliasContaining(String userAlias, Pageable pageable);

}
