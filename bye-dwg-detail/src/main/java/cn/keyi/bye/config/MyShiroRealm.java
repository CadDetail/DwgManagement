package cn.keyi.bye.config;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import cn.keyi.bye.model.SysPermission;
import cn.keyi.bye.model.SysRole;
import cn.keyi.bye.model.SysUser;
import cn.keyi.bye.service.SysUserService;

public class MyShiroRealm extends AuthorizingRealm {
	
	@Resource
	private SysUserService sysUserService;

	/**
	 * 用于获取用户权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser userInfo  = (SysUser)principals.getPrimaryPrincipal();
        for(SysRole role:userInfo.getRoles()) {
            authorizationInfo.addRole(role.getRoleName());
            for(SysPermission p:role.getPermissions()) { 
            	authorizationInfo.addStringPermission(p.getPermissionCode());
            }
        }       
        return authorizationInfo;
	}

	/**
	 * 用来进行身份认，即验证用户输入的账号和密码是否正确
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        // 获取登录用户的账户
		// String userName = (String) token.getPrincipal();	// 登录输入的账号
        // String password = new String((char[]) token.getCredentials());	// 登录输入的密码
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();	// 登录输入的账号
		String password = String.valueOf(token.getPassword());	// 登录输入的密码
        SysUser user = sysUserService.findByUserName(userName);	// 从数据库中查询此账号信息
        if (null == user) {
            throw new UnknownAccountException();
        } else {
        	// 使用md5方法对登录密码进行加密，便于与与数据库中存储的密码进行匹配
        	int hashIterations = 2;
            String credentials = sysUserService.generatePassword(password, user.getCredentialsSalt(), hashIterations);
            token.setPassword(credentials.toCharArray());
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        }
        // 通过 username 从数据库中查找  User 对象，如果找到则进一步获取其身份认证信息，没找到返回 null
        // 这里可以根据实际情况做缓存，如果不做，Shiro 自己也有时间间隔机制，2分钟内不会重复执行该方法
        // SysUser userInfo = sysUserService.findByUserName(userName);
        // if(userInfo == null) {
        //     return null;
        // }
        // System.out.println("userName:" + userInfo.getUserName() + ", password:" + userInfo.getPassword());
        // SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
        // 	userInfo, // 用户
        // 	userInfo.getPassword(), // 密码
        // 	ByteSource.Util.bytes(userInfo.getCredentialsSalt()), // 密码盐：userName + salt
        // 	getName()  // realm name
        // );
        // return authenticationInfo;
	}

}
