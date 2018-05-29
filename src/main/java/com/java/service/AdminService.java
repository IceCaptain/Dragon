package com.java.service;

import com.java.entity.Admin;

/**
 * 管理员Service接口
 * @author zxy
 *
 */
public interface AdminService {
	
	/**
	 * 管理员登录
	 * @param userName
	 * @param password
	 * @return
	 */
	public Admin login(String userName,String password);
	
	/**
	 * 根据Id查询管理员信息
	 * @param id
	 * @return
	 */
	public Admin getById(Integer id);
	
	/**
	 * 修改管理员信息
	 * @param admin
	 */
	public void save(Admin admin);

}
