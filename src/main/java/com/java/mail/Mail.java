package com.java.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * 邮件抽象类
 * @author zxy
 *
 */
public abstract class Mail implements Runnable {
	
	private static final String from = "xxxxxxxx";// 发件人电子邮箱
	private static final String license="xxx";//发件人授权码
	private static final String host = "smtp.163.com"; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com/pop.163.com/imap.163.com(网易)
	private static final String address="http://topgun.xicp.io";//指定发送邮件的服务器
	
	private String email;//收件人邮箱
	
	private String subject;//邮件标题
	
	private String content;//邮件内容
	
	public Mail(String email) {
		super();
		this.email = email;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static String getAddress() {
		return address;
	}

	public static void send(Mail mail){
		Thread t=new Thread(mail);
		try{
			t.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendEmail(email);
	}
	
	public void sendEmail(String email){
		// 1.创建连接对象javax.mail.Session
		// 2.创建邮件对象 javax.mail.Message
		// 3.发送一封激活邮件

		Properties properties = System.getProperties();// 获取系统属性

		properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
		properties.setProperty("mail.smtp.auth", "true");// 打开认证

		try{
			// QQ邮箱需要下面这段代码，163邮箱不需要
//			MailSSLSocketFactory sf = new MailSSLSocketFactory();
//			sf.setTrustAllHosts(true);
//			properties.put("mail.smtp.ssl.enable", "true");
//			properties.put("mail.smtp.ssl.socketFactory", sf);

			// 1.获取默认session对象 aigthmjzfbmgbehj kwipilaimjcmbbfj 
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, license); // 发件人邮箱账号、授权码
				}
			});

			// 2.创建邮件对象
			Message message = new MimeMessage(session);
			// 2.1设置发件人
			message.setFrom(new InternetAddress(from));
			// 2.2设置接收人
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			// 2.3设置邮件主题
			String subject = MimeUtility.encodeWord(this.subject, "UTF-8", "Q");
			message.setSubject(subject);
			// 2.4设置邮件内容
			String content = this.content;

			message.setContent(content, "text/html;charset=UTF-8");
			// 3.发送邮件
			Transport.send(message);
			System.out.println("邮件成功发送!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
