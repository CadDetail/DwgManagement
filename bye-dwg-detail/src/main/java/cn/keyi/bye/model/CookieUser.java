package cn.keyi.bye.model;

import java.io.Serializable;
import java.util.HashSet;

public class CookieUser implements Serializable {

	private static final long serialVersionUID = -3933931739204737272L;	
	private Long userId;
	private String userName;
	private HashSet<String> roleList;
	private HashSet<String> permissionList;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public HashSet<String> getRoleList() {
		return roleList;
	}
	public void setRoleList(HashSet<String> roleList) {
		this.roleList = roleList;
	}
	public HashSet<String> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(HashSet<String> permissionList) {
		this.permissionList = permissionList;
	}

}
