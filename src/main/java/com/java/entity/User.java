package com.java.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户实体
 * @author zxy
 *
 */
@Entity
@Table(name="t_user")
public class User {
	
	@Id
	@GeneratedValue
	private Integer id;//编号
	
	@Column(length=20)
	private String userName;//用户名
	
	@Column(length=20)
	private String password;//密码
	
	@Column(length=20)
	private String trueName;//真实姓名
	
	@Column(length=50)
	private String email;//邮箱
	
	private Integer role;
	
	private Integer state;//激活状态 0未激活 1已激活
	
	private String code;//激活码 用户名的MD5加密

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

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
