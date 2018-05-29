package com.java.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java.entity.User;
import com.java.repository.UserRepository;
import com.java.service.UserService;
import com.java.util.StringUtil;

/**
 * 用户Service接口实现类
 * @author zxy
 *
 */
@Service("userService")
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserRepository userRepository;

	@Override
	public List<Integer> findIdByName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findIdByName(userName);
	}

	@Override
	public User getByName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findOne(new Specification<User>() {
			
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(StringUtil.isNotEmpty(userName)){
					predicate.getExpressions().add(cb.equal(root.get("userName"), userName));
				}
				return predicate;
			}
		});
	}

	@Override
	public User getById(Integer id) {
		// TODO Auto-generated method stub
		return userRepository.findOne(id);
	}

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
	}
	

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public List<User> list(User user,Integer page,Integer pageSize) {
		// TODO Auto-generated method stub
		Page<User> pageUser=userRepository.findAll(new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(user!=null){
					if(StringUtil.isNotEmpty(user.getUserName())){
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName()+"%"));
					}
					if(StringUtil.isNotEmpty(user.getTrueName())){
						predicate.getExpressions().add(cb.like(root.get("trueName"), "%"+user.getTrueName()+"%"));
					}
				}
				return predicate;
			}
		}, new PageRequest(page-1, pageSize));
		return pageUser.getContent();
	}

	@Override
	public Long getCount(User user) {
		// TODO Auto-generated method stub
		Long total=userRepository.count(new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate predicate=cb.conjunction();
				if(user!=null){
					if(StringUtil.isNotEmpty(user.getUserName())){
						predicate.getExpressions().add(cb.like(root.get("userName"), "%"+user.getUserName()+"%"));
					}
					if(StringUtil.isNotEmpty(user.getTrueName())){
						predicate.getExpressions().add(cb.like(root.get("trueName"), "%"+user.getTrueName()+"%"));
					}
				}
				return predicate;
			}
		});
		return total;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		userRepository.delete(id);
	}


}
