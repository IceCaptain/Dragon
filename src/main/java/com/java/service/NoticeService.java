package com.java.service;

import java.util.List;

import com.java.entity.Notice;

/**
 * 通知Service接口
 * @author zxy
 *
 */
public interface NoticeService {
	
	/**
	 * 分页查询通知信息
	 * @return
	 */
	public List<Notice> list(Integer page, Integer pageSize);
	
	/**
	 * 获取总记录数
	 * @return
	 */
	public Long getCount();
	
	/**
	 * 根据Id查询通知信息
	 * @param id
	 * @return
	 */
	public Notice getById(Integer id);
	
	/**
	 * 根据Id删除通知
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 添加或修改通知信息
	 * @param notice
	 */
	public void save(Notice notice);

}
