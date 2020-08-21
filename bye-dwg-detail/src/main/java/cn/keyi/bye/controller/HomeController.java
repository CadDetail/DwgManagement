package cn.keyi.bye.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.keyi.bye.service.ArtifactService;
import cn.keyi.bye.service.SysPermissionService;
import cn.keyi.bye.service.SysRoleService;
import cn.keyi.bye.service.SysUserService;

/**
 * comment :用于处理登录及主页跳转相关请求
 * author  :兴有林栖
 * date    :2020-8-1
 */
@Controller
public class HomeController {

	@Autowired
	SysUserService sysUserService;
	@Autowired
	SysRoleService sysRoleService;
	@Autowired
	SysPermissionService sysPermissionService;
	@Autowired
	ArtifactService artifactService;
	
	
//	@RequestMapping("/login")
//	public String doLogin(HttpServletRequest request, Model model) {
//		// 若登录失败，则从 request 中获取 shiro 处理的异常信息，shiroLoginFailure 为  shiro 异常类的全类名   
//        String error = "";
//        String exception = (String) request.getAttribute("shiroLoginFailure");
//        // System.out.println(exception);
//        if(exception != null) {
//        	if(exception.contains("UnknownAccountException")) {
//        		error = "账号不存在";
//        	} else if(exception.contains("IncorrectCredentialsException")) {
//        		error = "密码不正确";
//        	} else {
//        		error = exception;
//        	}	        
//		}
//		model.addAttribute("error", error); 
//        // 此方法不处理登录成功，由 shiro 自己进行处理
//		return "login";
//    }
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> postLogin(String username, String password, Boolean rememberMe, Model model) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String errorMsg = "登录成功！";		
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
			SecurityUtils.getSubject().login(token);
			resultMap.put("status", 1);
		} catch (AccountException e) {
			errorMsg = "输入的账号不存在！";
			resultMap.put("status", 0);
		} catch (CredentialsException e) {
			errorMsg = "输入的密码不正确！";
			resultMap.put("status", 0);
		} catch (Exception e) {
			errorMsg = e.getMessage();
		}
		resultMap.put("message", errorMsg);
		return resultMap;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET) 
	public void getLogin() {
		
	}
	
	@RequestMapping({"/", "/index"})
    public String index() {
        return "index";
    }	

	@RequestMapping("/user")
	@RequiresPermissions("user:view")
    public String user() {
        return "user";
    }
	
	@RequestMapping("/role")
	@RequiresPermissions("role:view")
    public String role() {
        return "role";
    }
	
	@RequestMapping("/permission")
	@RequiresPermissions("permission:view")
    public String permission() {
        return "permission";
    }
	
	@RequestMapping("/artifact")
	@RequiresPermissions("artifact:view")
    public String artifact() {
        return "artifact";
    }
	
	@RequestMapping("/detail")
	@RequiresPermissions("detail:view")
    public String detail() {
        return "detail";
    }
	
	@RequestMapping("/importdetail")
	@RequiresPermissions("detail:import")
    public String importdetail() {
        return "importdetail";
    }
	
	@RequestMapping("/getCountInfo")
	@ResponseBody
	public Object getCountInfo() {
		Map<String, Object> map = new HashMap<>();
		map.put("user", sysUserService.getUserCount());
		map.put("role", sysRoleService.getRoleCount());
		map.put("permission", sysPermissionService.getPermissionCount());
		map.put("product", artifactService.getArtifactCount((short) 0));
		map.put("artifact", artifactService.getArtifactCount());
		return map;
	}
	
}
