package com.java.service;

import java.util.List;

import com.java.entity.User;

/**
 * 用户Service接口
 * @author zxy
 *
 */
public interface UserService {
	
	/**
	 * 通过用户名模糊查询用户信息
	 * @param userName
	 * @return
	 */
	public List<Integer> findIdByName(String userName);
	
	/**
	 * 根据用户名查询用户信息
	 * @param userName
	 * @return
	 */
	public User getByName(String userName);
	
	/**
	 * 根据Id查询用户信息
	 * @param id
	 * @return
	 */
	public User getById(Integer id);
	
	/**
	 * 添加或修改用户信息
	 * @param user
	 */
	public void save(User user);
	
	/**
	 * 查询所有用户信息
	 * @return
	 */
	public List<User> findAll();
	
	/**
	 * 分页查询用户信息
	 * @param user
	 * @return
	 */
	public List<User> list(User user,Integer page,Integer pageSize);
	
	/**
	 * 获取总记录数
	 * @param user
	 * @return
	 */
	public Long getCount(User user);
	
	/**
	 * 删除指定Id的用户信息
	 * @param id
	 */
	public void delete(Integer id);

}
