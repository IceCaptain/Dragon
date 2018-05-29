package com.java.mail;

/**
 * 通知邮件
 * @author zxy
 *
 */
public class MailInform extends Mail {

	private String info;//通知信息

	public MailInform(String email, String info) {
		super(email);
		this.info = info;
		setSubject("用户通知");
		setContent("这是一封Topgun用户平台通知邮件，请注意查收！<br/>" +info);
	}

}
