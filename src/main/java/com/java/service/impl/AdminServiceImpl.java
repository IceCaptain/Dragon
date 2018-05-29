package com.java.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.entity.Admin;
import com.java.repository.AdminRepository;
import com.java.service.AdminService;

/**
 * 管理员Service接口实现类
 * @author zxy
 *
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService{
	
	@Resource
	private AdminRepository adminRepository;

	@Override
	public Admin login(String userName, String password) {
		// TODO Auto-generated method stub
		return adminRepository.login(userName, password);
	}

	@Override
	public Admin getById(Integer id) {
		// TODO Auto-generated method stub
		return adminRepository.findOne(id);
	}

	@Override
	public void save(Admin admin) {
		// TODO Auto-generated method stub
		adminRepository.save(admin);
	}

}
