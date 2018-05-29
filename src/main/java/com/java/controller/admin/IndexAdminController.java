package com.java.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理员根目录请求
 * @author zxy
 *
 */
@Controller
public class IndexAdminController {
	
	/**
	 * 重定向到登录路径
	 * @return
	 */
	@RequestMapping("/admin")
	public String root(){
		return "/admin/login";
	}

}
