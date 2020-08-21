package cn.keyi.bye.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;


@Configuration
public class ShiroConfig {
	
	/**
	 * @ shiro的过滤器
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);	// 安全管理器
		shiroFilterFactoryBean.setLoginUrl("/login");	// 登录页面		
		shiroFilterFactoryBean.setSuccessUrl("/index");	// 登录成功后要跳转的链接		
		shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");	// 未授权页面				
		// 过滤链定义, 从上向下顺序执行, 一般将 /** 放在最为下边
	    // authc: 所有 url 都必须认证通过才可以访问；anon: 所有 url 都可以匿名访问
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String, String>();	
		filterChainDefinitionMap.put("/favicon.ico"	, "anon");
		filterChainDefinitionMap.put("/login"		, "anon");
		filterChainDefinitionMap.put("/logout"		, "logout");	// 退出过滤器, 具体退出代码由Shiro自身实现
		filterChainDefinitionMap.put("/static/**"	, "anon");		
		filterChainDefinitionMap.put("/css/**"		, "anon");
		filterChainDefinitionMap.put("/img/**"		, "anon");
		filterChainDefinitionMap.put("/js/**"		, "anon");
		filterChainDefinitionMap.put("/plugins/**"	, "anon");
		filterChainDefinitionMap.put("/files/**"	, "anon");
		// user权限用于配置“记住我”或认证通过可以访问
		filterChainDefinitionMap.put("/"			, "user");		
		filterChainDefinitionMap.put("/index"		, "user");
		filterChainDefinitionMap.put("/userInfo/**"	, "user");
		// 对于“记住我”, 只允许进入到首页, 其它功能必须重新登录
		filterChainDefinitionMap.put("/**"			, "authc");	
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);	// 拦截器
		return shiroFilterFactoryBean;
	}

	/**
	 * 自定义身份认证 realm，目的是注入 myShiroRealm
	 * @return
	 */
	@Bean
	public MyShiroRealm myShiroRealm(){
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		return myShiroRealm;
	}

	/**
	 * 注入 securityManager
	 * @return
	 */
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
		// 设置realm
		 securityManager.setRealm(myShiroRealm());
		// 注入记住我管理器
	    securityManager.setRememberMeManager(rememberMeManager());		
		return securityManager;
	}
	
	/**
	 * @ cookie对象
	 * @return
	 */
	public SimpleCookie rememberMeCookie(){
	   // 这个参数是cookie的名称，对应前端的 checkbox 的 name = rememberMe
	   SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
	   // 记住我cookie生效时间1天，单位秒
	   simpleCookie.setMaxAge(86400);
	   return simpleCookie;
	}

	/**
	 * @ cookie管理对象——记住我功能
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager(){
	   CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
	   cookieRememberMeManager.setCookie(rememberMeCookie());
	   //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
	   cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
	   return cookieRememberMeManager;
	}
	
	/*
	 * @ 在方法内部对异常的类型进行判断, 然后返回合适的 ModelAndView 对象
	 */
	@Bean(name="simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("DatabaseException", "databaseError");		// 数据库异常处理
		mappings.setProperty("UnauthorizedException", "unauthorized");	// 授权异常处理
		r.setExceptionMappings(mappings);  // None by default
		r.setDefaultErrorView("error");    // 为所有的异常定义默认的异常处理页面，exceptionMappings未定义的异常使用本默认配置
		r.setExceptionAttribute("exception");     // 定义异常处理页面用来获取异常信息的变量名，默认名为exception
		return r;
	}

	/**
	 * @ 开启 Shiro aop 注解支持（因为使用代理方式，所以需要开启代码支持）
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * @ 为了在thymeleaf中使用shiro的自定义tag
	 * @return
	 */
	@Bean(name="shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }	
	
	
}
