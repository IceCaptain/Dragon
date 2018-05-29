package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.Admin;

/**
 * 管理员Repository接口
 * @author zxy
 *
 */
public interface AdminRepository extends JpaRepository<Admin, Integer>,JpaSpecificationExecutor<Admin>{
	
	/**
	 * 管理员登录
	 * @param userName
	 * @param password
	 * @return
	 */
	//mysql 加上  binary ，规定区分大小写，sqlite 不需要
	@Query(value="select * from t_admin where user_name=?1 and password=?2",nativeQuery=true)
	public Admin login(String userName,String password);

}
