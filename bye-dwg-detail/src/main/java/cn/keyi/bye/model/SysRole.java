package cn.keyi.bye.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="sysrole")
public class SysRole implements Serializable {
	private static final long serialVersionUID = 2171365480973491163L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleId")
	private Long roleId;
	// 角色名称，用于在代码中处理
	@Column(unique = true, name = "roleName")
	private String roleName;
	// 角色别名，用于在 UI 中显示
	private String description;
	// 此角色可查询的已经会签过的指定工序的明细
	private String workingsteps;
	// 是否可用
	@Column(name = "roleAvailable")
	private Boolean roleAvailable = Boolean.TRUE;	
	// 角色包含的权限列表。角色——权限：多对多关系。一个角色拥有多个权限
    @ManyToMany(fetch = FetchType.EAGER)	// 立刻加载，在查询主对象的时候同时加载关联对象
    @JoinTable(name = "role_permission", joinColumns = {@JoinColumn(name = "roleId")}, 
    	inverseJoinColumns = {@JoinColumn(name = "permissionId")})
    private List<SysPermission> permissions;
    // 角色包含的用户列表。角色——用户：多对多关系。一个角色包含多个用户
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "roleId")}, 
    	inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<SysUser> users;
    
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWorkingsteps() {
		return workingsteps;
	}
	public void setWorkingsteps(String workingsteps) {
		this.workingsteps = workingsteps;
	}
	public Boolean getRoleAvailable() {
		return roleAvailable;
	}
	public void setRoleAvailable(Boolean roleAvailable) {
		this.roleAvailable = roleAvailable;
	}
	public List<SysPermission> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<SysPermission> permissions) {
		this.permissions = permissions;
	}
	public List<SysUser> getUsers() {
		return users;
	}
	public void setUsers(List<SysUser> users) {
		this.users = users;
	}
}
