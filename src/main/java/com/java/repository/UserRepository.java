package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java.entity.User;

/**
 * 用户Repository接口
 * @author zxy
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User>{
	
	/**
	 * 通过用户名模糊查询用户Id
	 * @param userName
	 * @return
	 */
	@Query(value="select id from t_user where user_name like %?1%",nativeQuery=true)
	public List<Integer> findIdByName(String userName);

}
