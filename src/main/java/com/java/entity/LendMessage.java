package com.java.entity;

import java.util.List;

/**
 * 归还图书后为第一预约人借阅保留信息
 * @author zxy
 *
 */
public class LendMessage {
	
	private String userName;//用户名
	
	private List<String> messageList;//保留信息

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<String> messageList) {
		this.messageList = messageList;
	}

}
