package com.java.entity;

/**
 * 图书借阅计时状态
 * @author zxy
 *
 */
public class BookTimerState {
	
	private String name;//图书名称
	
	private Integer state;//借阅计时状态 0：正常计时  1：成功还书，计时中断

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
