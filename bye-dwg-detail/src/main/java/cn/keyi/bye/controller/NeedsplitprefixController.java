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
import cn.keyi.bye.model.Needsplitprefix;
import cn.keyi.bye.service.NeedsplitprefixService;

@RestController
@RequestMapping("/needsplitprefix")
public class NeedsplitprefixController {
	
	@Autowired
	NeedsplitprefixService needsplitprefixService;
	
	@RequestMapping("/findNeedsplitprefix")
	@RequiresPermissions("needsplitprefix:view")
	public List<Needsplitprefix> findNeedsplitprefix() {
		return needsplitprefixService.getNeedsplitprefixs();
	}
	
	@RequestMapping("/saveNeedsplitprefix")
	@RequiresPermissions(value={"workingsteps:add","workingsteps:edit"},logical=Logical.OR)
	public Object saveNeedsplitprefix(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		CookieUser cookieUser = (CookieUser) subject.getPrincipal();
		Needsplitprefix needsplitprefix = new Needsplitprefix();
		if(request.getParameter("id") != null) {
			Long id = Long.valueOf(request.getParameter("id"));
			Needsplitprefix tmp = needsplitprefixService.getNeedsplitprefixById(id);
			if(tmp != null) {
				needsplitprefix = tmp;
			} else {
				needsplitprefix.setPrefixId(id);
			}
			needsplitprefix.setUpdateBy(cookieUser.getUserName());
			needsplitprefix.setUpdateTime(LocalDateTime.now());
		} else {
			needsplitprefix.setCreateBy(cookieUser.getUserName());
			needsplitprefix.setCreateTime(LocalDateTime.now());
		}
		needsplitprefix.setPrefixLabel(request.getParameter("prefixLabel"));
		needsplitprefix.setPrefixProduct(Boolean.valueOf(request.getParameter("prefixProduct")));
		Map<String, Object> map = new HashMap<String, Object>();
		String rslt = needsplitprefixService.saveNeedsplitprefix(needsplitprefix);
		if(rslt.isEmpty()) {
			map.put("status", 1);
			map.put("message", "图号标签保存成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;		
	}
	
	@RequestMapping("/deleteNeedsplitprefix")
	@RequiresPermissions("workingsteps:del")
	public Object deleteNeedsplitprefix(Long prefixId) {
		Map<String,Object> map = new HashMap<String, Object>();
		String rslt = needsplitprefixService.deleteNeedsplitprefix(prefixId);
		if(rslt.isEmpty()) {
			map.put("status",1);
			map.put("massage","图号标签删除成功！");
		} else {
			map.put("status", 0);
			map.put("message", rslt);
		}
		return map;
	}

}
