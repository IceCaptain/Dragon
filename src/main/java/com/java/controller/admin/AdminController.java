package com.java.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.java.entity.Admin;
import com.java.entity.User;
import com.java.mail.Mail;
import com.java.mail.MailAudit;
import com.java.runner.StartupRunner;
import com.java.service.AdminService;
import com.java.service.UserService;
import com.java.util.PageUtil;
import com.java.util.StringUtil;

/**
 * 管理员Controller层
 * @author zxy
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Resource
	private AdminService adminService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private StartupRunner startupRunner;
	
	/**
	 * 管理员登录
	 * @param admin
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login(Admin admin,HttpServletRequest request){
		ModelAndView mav=new ModelAndView();
		if(StringUtil.isEmpty(admin.getUserName())){
			mav.addObject("error", "用户名不能为空！");
			mav.setViewName("/admin/login");
		}else if(StringUtil.isEmpty(admin.getPassword())){
			mav.addObject("userName", admin.getUserName());
			mav.addObject("error", "密码不能为空！");
			mav.setViewName("/admin/login");
		}else{
			Admin currentUser=adminService.login(admin.getUserName(), admin.getPassword());
			if(currentUser==null){
				mav.addObject("userName", admin.getUserName());
				mav.addObject("password", admin.getPassword());
				mav.addObject("error", "用户名或密码错误！");
				mav.setViewName("/admin/login");
			}else{
				HttpSession session=request.getSession();
				session.setAttribute("currentUser", currentUser);
				mav.addObject("title", "主页");
				mav.addObject("menuTitle", "主页");
				mav.addObject("mainPage", "/admin/common/default");
				mav.addObject("mainPageKey", "#d");
				mav.setViewName("/admin/index");
			}
		}
		return mav;
	}
	
	/**
	 * 刷新系统缓存
	 * @return
	 */
	@RequestMapping("/refresh")
	@ResponseBody
	public Map<String,Object> refresh(){
		Map<String,Object> map=new HashMap<String,Object>();
		startupRunner.loadData();
		map.put("success", "刷新成功！");
		return map;
	}
	
	/**
	 * 跳转到管理员修改密码页面
	 * @param notice
	 * @return
	 */
	@RequestMapping("/preModifyPwd")
	public ModelAndView preModifyPwd(){
		ModelAndView mav=new ModelAndView();
		mav.addObject("title", "密码修改");
		mav.addObject("menuTitle", "系统管理→密码修改");
		mav.addObject("mainPage", "/admin/personal/modifyPwd");
		mav.addObject("mainPageKey", "#p");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 管理员修改密码
	 * @param id
	 * @return
	 */
	@RequestMapping("/modifyPwd")
	@ResponseBody
	public Map<String,Object> modifyPwd(@RequestParam("id")Integer id,@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword){
		Map<String,Object> map=new HashMap<String,Object>();
		Admin admin=adminService.getById(id);
		if(!admin.getPassword().equals(oldPassword)){
			map.put("error", "原密码错误！");
		}else{
			admin.setPassword(newPassword);
			adminService.save(admin);
			map.put("success", "密码修改成功！");
		}
		return map;
	}
	
	/**
	 * 管理员注销
	 * @return
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public Map<String,Object> logout(HttpServletRequest request){
		Map<String,Object> map=new HashMap<String,Object>();
		request.getSession().removeAttribute("currentUser");
		map.put("success", true);
		return map;
	}
	
	@RequestMapping("/user/list/{page}")
	public ModelAndView userList(@PathVariable("page")Integer page,User user){
		ModelAndView mav=new ModelAndView();
		int pageSize=8;
		List<User> userList=userService.list(user, page, pageSize);
		Long total=userService.getCount(user);
		
		StringBuffer param=new StringBuffer();
		if(user!=null){
			if(StringUtil.isNotEmpty(user.getUserName())){
				param.append("userName="+user.getUserName()+"&");
			}
			if(StringUtil.isNotEmpty(user.getTrueName())){
				param.append("trueName="+user.getTrueName()+"&");
			}
		}
		mav.addObject("userList", userList);
		mav.addObject("pageCode", PageUtil.genPagination("/admin/user/list", total, page, pageSize, param.toString()));
		mav.addObject("s_user", user);
		mav.addObject("title", "用户信息");
		mav.addObject("menuTitle", "用户管理→用户信息");
		mav.addObject("mainPage", "/admin/user/list");
		mav.addObject("mainPageKey", "#u");
		mav.setViewName("/admin/index");
		return mav;
	}
	
	/**
	 * 删除未激活用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/user/delete")
	@ResponseBody
	public Map<String,Object> userDelete(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		User user=userService.getById(id);
		if(user.getState()==1){
			map.put("error", "已激活用户不能删除！");
		}else{
			userService.delete(id);
			map.put("success", "删除成功！");
		}
		return map;
	}
	
	/**
	 * 发送激活邮件到用户邮箱
	 * @return
	 */
	@RequestMapping("/user/audit")
	@ResponseBody
	public Map<String,Object> userAudit(@RequestParam("id")Integer id){
		Map<String,Object> map=new HashMap<String,Object>();
		User user=userService.getById(id);
		if(user.getState()==1){
			map.put("error", "用户已激活，请勿重复激活！");
		}else{
			Mail.send(new MailAudit(user.getEmail(), user.getCode()));
			map.put("success", "激活邮件已发送到用户邮箱！");
		}
		return map;
	}
	
	/**
	 * 用户信息审核通过 更改为激活状态
	 * @param code
	 * @return
	 */
	@RequestMapping("/audit/pass")
	public String auditPass(@RequestParam("code")String code){
		List<User> userList=userService.findAll();
		for(User user:userList){
			if(user.getCode()!=null){
				if(user.getCode().equals(code)){
					user.setState(1);
					userService.save(user);
					return "/user/audit/auditSuccess";
				}
			}
		}
		return "/user/audit/auditFailure";
	}

}
