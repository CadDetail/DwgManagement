package cn.keyi.bye.service;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.keyi.bye.dao.SysUserDao;
import cn.keyi.bye.model.SysUser;

@Service
public class SysUserService {
	
	@Autowired
	SysUserDao sysUserDao;
	
	/**
	 * 根据用户名（唯一）查找用户信息
	 * @param userName
	 * @return
	 */
	public SysUser findByUserName(String userName) {
		return sysUserDao.findByUserName(userName);
	}
	
	/**
	 * 根据ID查询用户信息
	 * @param userId
	 * @return
	 */
	public SysUser findByUserId(Long userId) {		
		SysUser findResult = sysUserDao.findByUserId(userId);
		return findResult;
	}
	
	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	public String saveUser(SysUser user) {
		String rslt = "";
		try {
			sysUserDao.save(user);
		} catch (Exception e) {
			rslt = e.getMessage();
		}
		return rslt;
	}
	
	/**
	 * 根据用户姓名模糊查询对应的用户列表
	 * @param userAlias
	 * @param pageable
	 * @return
	 */
	public Page<SysUser> getUsersByPage(String userAlias, Pageable pageable) {
		return sysUserDao.findByUserAliasContaining(userAlias, pageable);
	}
	
	/**
	 * 根据ID删除用户
	 * @param userId
	 * @return
	 */
	public String deleteUser(Long userId) {
		String rslt = "";
		try {
			sysUserDao.deleteById(userId);
		} catch (Exception e) {		
			rslt = e.getMessage();
			if(rslt.contains("ConstraintViolationException")) {
				rslt = "违反参照完整性，请先删除从表中的相关记录！";
			}
		}
		return rslt;
	}
	
	/**
	 * 根据salt对明文密码进行加密
	 * @param originalPassword
	 * @param salt
	 * @param hashIterations, 散列的次数，比如散列两次，相当于 md5(md5(""));
	 * @return
	 */
	public String generatePassword(String originalPassword, String salt, int hashIterations) {
		SimpleHash passwordHash = new SimpleHash("md5", originalPassword, ByteSource.Util.bytes(salt), hashIterations);
		return passwordHash.toHex();
	}
	
	/**
	 * 用户数量
	 * @return
	 */
	public Long getUserCount() {
		return sysUserDao.count();
	}
}
