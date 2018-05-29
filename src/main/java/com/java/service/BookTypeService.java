package com.java.service;

import java.util.List;

import com.java.entity.BookType;

/**
 * 图书类别Service接口
 * @author zxy
 *
 */
public interface BookTypeService {
	
	/**
	 * 获取图书类别以及对应图书数量
	 * @return
	 */
	public List<BookType> countList();
	
	/**
	 * 查询图书类别信息
	 * @return
	 */
	public List<BookType> list();
	
	/**
	 * 分页查询图书类别信息
	 * @return
	 */
	public List<BookType> list(BookType bookType,Integer page,Integer pageSize);
	
	/**
	 * 获取总记录数
	 * @return
	 */
	public Long getCount();
	
	/**
	 * 根据Id查询图书类别信息
	 * @param id
	 * @return
	 */
	public BookType getById(Integer id);
	
	/**
	 * 添加或修改图书类别
	 * @param bookType
	 */
	public void save(BookType bookType);
	
	/**
	 * 根据Id删除图书类别信息
	 * @param id
	 */
	public void delete(Integer id);

}
