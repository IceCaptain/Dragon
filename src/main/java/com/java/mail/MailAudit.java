package com.java.mail;

/**
 * 激活邮件
 * @author zxy
 *
 */
public class MailAudit extends Mail {

	private String code;//激活码
	
	public MailAudit(String email, String code) {
		super(email);
		this.code = code;
		setSubject("账号激活");
		setContent("<html><head></head><body><h1>这是一封Topgun用户平台激活邮件,激活请点击以下链接</h1><h3><a target='_blank' href='"+getAddress()+"/admin/audit/pass?code="
					+ code + "'>"+getAddress()+"/admin/audit/pass?code=" + code + "</a></h3></body></html>");
	}

}
