package cn.keyi.bye.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.keyi.bye.model.CookieUser;
import cn.keyi.bye.model.Workingsteps;
import cn.keyi.bye.service.WorkingstepsService;

@RestController
@RequestMapping("/workingsteps")
public class WorkingstepsController {

	@Autowired
	WorkingstepsService workingstepsService;
	
	@RequestMapping("/findWorkingsteps")
	@RequiresPermissions(value={"system:all","workingsteps:view","detail:check"},logical=Logical.OR)
	public List<Workingsteps> findWorkingsteps() {
		return workingstepsService.getAllWorkingsteps();
	}
	
	@RequestMapping("/saveWorkingsteps")
	@RequiresPermissions(value={"system:all","workingsteps:add","workingsteps:edit"},logical=Logical.OR)
	public Object saveWorkingsteps(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		Workingsteps workingsteps = new Workingsteps();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			Workingsteps tmp = workingstepsService.getWorkingstepsById(id);
			if(tmp != null) {
				workingsteps = tmp;
			} else {
				workingsteps.setStepId(id);
			}
			workingsteps.setUpdateBy(cookieUser.getUserName());
			workingsteps.setUpdateTime(LocalDateTime.now());
		} else {
			workingsteps.setCreateBy(cookieUser.getUserName());
			workingsteps.setCreateTime(LocalDateTime.now());
		}
		workingsteps.setStepName(request.getParameter("workingstepName"));
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = workingstepsService.saveWorkingsteps(workingsteps);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "工序保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;		
	}
	
	@RequestMapping("/deleteWorkingsteps")
	@RequiresPermissions(value={"system:all","workingsteps:del"},logical=Logical.OR)
	public Object deleteWorkingsteps(Long stepId) {
		Map<String,Object> map = new HashMap<String, Object>();
		String rslt = workingstepsService.deleteWorkingsteps(stepId);
		if(rslt.isEmpty()) {
			map.put("status",1);
			map.put("massage","工序删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;
	}
	
}
