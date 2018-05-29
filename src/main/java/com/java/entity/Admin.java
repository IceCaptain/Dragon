package com.java.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 管理员实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_admin")
public class Admin {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@Column(length=20)
	private String userName;//用户名
	
	@Column(length=20)
	private String password;//密码

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
