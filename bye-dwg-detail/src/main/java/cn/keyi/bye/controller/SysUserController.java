package cn.keyi.bye.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.model.SysPermission;
import cn.keyi.bye.model.SysRole;
import cn.keyi.bye.model.SysUser;
import cn.keyi.bye.service.SysRoleService;
import cn.keyi.bye.service.SysUserService;

@RestController
@RequestMapping("/userInfo")
public class SysUserController {
	
	@Autowired
	SysUserService sysUserService;	
	@Autowired
	SysRoleService sysRoleService;
	
	/**
	 * comment: 从Shiro安全管理器里获取登录用户信息
	 * author : 兴有林栖
	 * date   : 2020-8-2
	 * @return
	 */
	@RequestMapping("/getLoggedUser")
	@RequiresUser
	public Object getLoggedUser() {
		// Subject subject = SecurityUtils.getSubject();
		// return (SysUser)subject.getPrincipal();
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		return sysUserService.findByUserName(cookieUser.getUserName());
	}
	
	/**
	 * comment: 修改登录用户密码
	 * author : 兴有林栖
	 * date   : 2020-8-2
	 * @param request
	 * @return
	 */
	@RequestMapping("/modifyPassword")
	@RequiresUser
	public Object modifyPassword(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String oldPassw = request.getParameter("oldpassword");
		String newPassw = request.getParameter("newpassword");
		// SysUser loggedUser = (SysUser)SecurityUtils.getSubject().getPrincipal();
		SysUser loggedUser = (SysUser) getLoggedUser();
		int hashIterations = 2;
		String oldPassword = sysUserService.generatePassword(oldPassw, loggedUser.getCredentialsSalt(), hashIterations);
		if(!oldPassword.equals(loggedUser.getPassword())) {
			map.put("status", 0);
			map.put("message", "原始密码不正确！");
		} else {
			String newPassword = sysUserService.generatePassword(newPassw, loggedUser.getCredentialsSalt(), hashIterations);
			loggedUser.setPassword(newPassword);
			String rslt = sysUserService.saveUser(loggedUser);
			if(rslt.isEmpty()) {
				map.put("status", 1);
				map.put("message", "密码修改成功！");
			} else {
				map.put("status", 0);
				map.put("message", rslt);
			}
		}		
		return map;		
	}
	
	/**
	 * comment: 获取登录用户的权限列表
	 * author : 兴有林栖
	 * date   : 2020-8-15
	 * @return
	 */
	@RequestMapping("/queryPermissions")
	@RequiresUser
    public Object queryPermissions() {
		List<String> permissionList = new ArrayList<String>();
		SysUser user = (SysUser) getLoggedUser();
        for(SysRole role: user.getRoles()) {
            for(SysPermission p: role.getPermissions()) {
            	permissionList.add(p.getPermissionCode());
            }
        } 
        return permissionList;
    }
	
	@RequestMapping("/getUsers")
	@RequiresPermissions("user:view")
	public Object getUsers(HttpServletRequest request) {
		Integer pageNumber = Integer.valueOf(request.getParameter("pageNumber")) - 1;
		Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
		if(pageNumber < 0) { pageNumber = 0; }
		//System.out.println(pageNumber);
		String userAlias = request.getParameter("userAlias");
		PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.DESC, "userId"));
		Page<SysUser> users = sysUserService.getUsersByPage(userAlias, pageable);
		return users;
	}
	
	@RequestMapping("/saveUser")
	@RequiresPermissions(value={"user:add","user:edit"},logical=Logical.OR)
	public Object saveUser(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUser user = null;
		// 修改模式
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			user = sysUserService.findByUserId(id);	
			if(user == null) {
				map.put("status", 0);
				map.put("message", "账号不存在，请重试！");
				return map;
			}
		}
		// 新增模式
		if(user == null) { 
			user = new SysUser();
			String name = request.getParameter("userName");
			SysUser existUser = sysUserService.findByUserName(name);
			if(existUser != null) {
				map.put("status", 0);
				map.put("message", "账号已存在，请重试！");
				return map;
			}
			user.setUserName(name);
			String password = request.getParameter("password");
			String salt = String.valueOf(System.currentTimeMillis());
			user.setSalt(salt);	
			user.setPassword(sysUserService.generatePassword(password, user.getCredentialsSalt(), 2));
			user.setSalt(salt);
			user.setState(Short.valueOf("1"));
		}
		// 新增和修改模式下，都需要设置用户姓名、所属角色等属性		
		user.setUserAlias(request.getParameter("userAlias"));		
		String roles = request.getParameter("userRole");
		List<SysRole> roleList = new ArrayList<SysRole>();
		for(String roleName: roles.split(",")) {
			SysRole role = sysRoleService.findByRoleName(roleName);
			if(role != null) {
				roleList.add(role);
			}
		}	
		user.setRoles(roleList);		
		String rslt = sysUserService.saveUser(user);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "用户保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
	@RequestMapping("/deleteUser")
	@RequiresPermissions("user:del")
	public Object deleteUser(Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = sysUserService.deleteUser(userId);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "用户删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
	@RequestMapping("/resetPassword")
	@RequiresPermissions(value={"user:add","user:edit"},logical=Logical.OR)
	public Object resetPassword(HttpServletRequest request, @RequestBody JSONObject params) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUser user = null;
		//Long id = Long.valueOf(request.getParameter("id"));
		Long id = params.getLong("id");
		user = sysUserService.findByUserId(id);	
		if(user == null) {
			map.put("status", 0);
			map.put("message", "账号不存在，请重试！");
			return map;
		}
		//String password = request.getParameter("password");
		String password = params.getString("password");
		user.setPassword(sysUserService.generatePassword(password, user.getCredentialsSalt(), 2));
		String rslt = sysUserService.saveUser(user);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "密码修改成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;	
	}
	
}
