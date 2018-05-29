package com.java.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * shiro MD5加密
 * @author Administrator
 *
 */
public class CryptographyUtil {

	
	/**
	 * Md5加密
	 * @param str 加密字段
	 * @param salt 盐
	 * @return
	 */
	public static String md5(String str,String salt){
		return new Md5Hash(str,salt).toString();
	}
	
	public static void main(String[] args) {
		String password="ccc";
		
		System.out.println("Md5加密"+CryptographyUtil.md5(password, "zxy"));
	}
}
