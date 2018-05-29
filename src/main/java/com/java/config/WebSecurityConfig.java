package com.java.config;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * spring security 配置
 * @author zxy
 *
 */
@Component
@EnableGlobalMethodSecurity(prePostEnabled=true)//开启security注解  部分方法需要ADMIN权限才能使用
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Resource
	private DataSource dataSource;

	/**
	 * 配置用户认证
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
//		super.configure(auth);
		auth.jdbcAuthentication().dataSource(dataSource)
		//mysql 加上  binary ，规定区分大小写，sqlite 不需要
		.usersByUsernameQuery("select user_name,password,role from t_user where user_name=? and state=1")
		.authoritiesByUsernameQuery("select user_name,role from t_user where user_name=? and state=1");
	}

	/**
	 * 配置请求授权
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.csrf().disable().cors().disable().headers().disable()
		.authorizeRequests()
		.antMatchers("/static/**","/admin/**","/user/preRegister","/user/userNameValidation","/user/register").permitAll()//不需要身份认证的请求路径
		.anyRequest().authenticated()//其他请求路径需要身份认证
		.and()
		.formLogin()
		.loginPage("/login")//指定登录请求路径
		.defaultSuccessUrl("/1")//登录成功的默认跳转页面
		.permitAll()
		.and()
		.logout()
		.logoutSuccessUrl("/login")//注销成功的默认跳转页面
		.permitAll();
	}
	
	/**
	 * 用户注销登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

}
